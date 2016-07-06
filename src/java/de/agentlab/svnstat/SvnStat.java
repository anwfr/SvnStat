/*
 * Copyright © 2006, 2014 Juergen Lind (jli@agentlab.de), 2014 Joe Egan (J0e3gan@gmail.com).
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 */

package de.agentlab.svnstat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PropertyResourceBundle;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;


public class SvnStat {

    private String repository;
    private String outdir;
    private String beginDate = null;
    private String endDate   = null;

    public boolean init(CLI cli) {
        String configfile = cli.getStringOption("config");
        PropertyResourceBundle config;
        try {
            if (configfile != null) {
                config = new PropertyResourceBundle(new FileInputStream(configfile));
            } else {
                config = new PropertyResourceBundle(Graph.class.getResourceAsStream("SvnStat.properties"));
            }
            Config.init(config);
            Graph.parseConfig();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        this.repository = cli.getStringOption("r");
        if (this.repository == null) {
            System.out.println("Repository or log file (-r option) must be provided!");
            return false;
        }

        this.outdir = cli.getStringOption("d", "svnstat");

        this.beginDate = cli.getStringOption("begin");
        this.endDate = cli.getStringOption("end");

        return true;
    }

    public void run() throws FileNotFoundException {
        InputStream in;
        if (this.repository.startsWith("http")) {
            in = this.getSvnLog(this.repository);
        } else {
            in = new FileInputStream(this.repository);
        }

        Stat stat = new Stat();

        try {
            this.readXml(in, stat);

            Collections.reverse(stat.getDates());

            stat.setDir(outdir);
            stat.setRepository(repository);

            boolean hasFiles = stat.fileCount(beginDate, endDate);

            if (!hasFiles) {
                System.out.println("No commits to analyze.");
                return;
            }

            stat.commitsPercentage(beginDate, endDate);

            stat.commitsAllUsers(beginDate, endDate);

            for (Iterator<String> i = stat.getUsers().iterator(); i.hasNext();) {
                String user = i.next();

                stat.commitsPerUser(beginDate, endDate, user);

                stat.changesPerUser(beginDate, endDate, user);

                stat.commitTimesPerUser(beginDate, endDate, user);

                stat.commitDaysPerUser(beginDate, endDate, user);

                stat.modulesPerUser(beginDate, endDate, user);
            }

            stat.moduleActivityPerUser();
            stat.moduleActivityPerUserPerDate();
            stat.commitsTotal(beginDate, endDate);

            this.writeIndexFile(stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readXml(InputStream in, Stat stat) throws IOException {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(in);
            for (Iterator<Content> i = document.getRootElement().getContent().iterator(); i.hasNext();) {
                Content logEntry = i.next();
                if (logEntry instanceof Element) {
                    String authorStr;

                    Element authorNode = ((Element) logEntry).getChild("author");
                    if (authorNode != null) {
                        authorStr = authorNode.getValue();
                    } else {
                        authorStr = "unknown";
                    }
                    String user;
                    if (authorStr.indexOf("=") != -1) {
                        user = authorStr.substring(authorStr.indexOf("=") + 1, authorStr.indexOf(",")).trim();
                    } else {
                      user = authorStr.trim();
                    }

                    String dateStr = ((Element) logEntry).getChild("date").getValue();

                    int index = dateStr.trim().indexOf("T");
                    String date = dateStr.trim().substring(0, index);
                    String time = dateStr.trim().substring(index + 1, index + 9);

                    SvnRecord record = new SvnRecord();

                    List nodes = XPath.selectNodes(logEntry, "paths/path[@action='A']");
                    for (Iterator j = nodes.iterator(); j.hasNext();) {
                        Element element = (Element) j.next();
                        record.addFile(element.getValue());
                    }
                    int added = nodes.size();

                    nodes = XPath.selectNodes(logEntry, "paths/path[@action='M']");
                    for (Iterator j = nodes.iterator(); j.hasNext();) {
                        Element element = (Element) j.next();
                        record.addFile(element.getValue());
                    }
                    int modified = nodes.size();

                    nodes = XPath.selectNodes(logEntry, "paths/path[@action='D']");
                    for (Iterator j = nodes.iterator(); j.hasNext();) {
                        Element element = (Element) j.next();
                        record.addFile(element.getValue());
                    }
                    int deleted = nodes.size();

                    record.setDate(date);
                    record.setTime(time);
                    record.setUser(user.toLowerCase());
                    record.setAdded(added);
                    record.setModified(modified);
                    record.setDeleted(deleted);
                    stat.addRecord(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InputStream getSvnLog(String url) {
        try {
            String cmd = "svn log --verbose --xml " + url;
            Process p = Runtime.getRuntime().exec(cmd);

            return p.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void writeIndexFile(Stat stat) {
        String content = "";

        String titleAndHeadline = Config.getProperty("Report.headline") + this.repository;

        content += "<!doctype html public \"-//W3C//DTD HTML 4.0 Transitional//EN\">";
        content += "<html>";
        content += "<head>";
        content += "<title>" + titleAndHeadline + "</title>";
        content += "</head>";

        content += "<body>";
        content += "<h2>" + titleAndHeadline + "</h2>";

        content += "<img src=\"Total_commits.jpg\">";
        content += "<img src=\"File_Count.jpg\">";

        content += "<br>";
        content += "<img src=\"AllUsers_commits.jpg\">";
        content += "<img src=\"Commit_Percentage.jpg\">";

        for (Iterator<String> i = stat.getUsers().iterator(); i.hasNext();) {
            String user = i.next();

            content += "<h2>" + user + "</h2>";
            content += "<img src=\"" + user + "_commits.jpg\">";
            content += "<img src=\"" + user + "_changes.jpg\">";
            content += "<img src=\"" + user + "_commitTimes.jpg\">";
            content += "<img src=\"" + user + "_commitDays.jpg\">";
        }

        content += "</body>";
        content += "</html>";

        try {
            PrintWriter p = new PrintWriter(new FileOutputStream(stat.getDir() + "/index.html"));
            p.println(content);
            p.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        if (args.length == 1 && args[0].equals("-help")) {
            printUsage();
            System.exit(0);
        }
        CLI cli = new CLI();
        cli.parseOptions(args);

        SvnStat stat = new SvnStat();

        try {
            if (stat.init(cli)) {
                stat.run();
            } else {
                printUsage();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Total time:" + (System.currentTimeMillis() - start));
    }

    private static void printUsage() {
        System.out.println(
            "Usage: SvnStat -r <repository/logfile> " +
                "[-d <outputDir>] " +
                "[-config <configfile>] " +
                "[-begin <date>] " +
                "[-end <date>]");
        System.out.println(
            "               (To use a logfile, the log must be retrieved using --verbose and --xml.)");
    }

}
