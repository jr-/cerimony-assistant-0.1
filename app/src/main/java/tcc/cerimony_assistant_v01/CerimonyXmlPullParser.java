package tcc.cerimony_assistant_v01;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.Environment;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import tcc.cerimony_assistant_v01.Cerimony;

/**
 * Created by zr on 31/01/17.
 */

public class CerimonyXmlPullParser {

    public static Cerimony getCerimonyFromFile(Context ctx, String filePath, Cerimony cerimony) {
        String creator;
        String curText = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            String root_sd = Environment.getExternalStorageDirectory().toString();
            BufferedReader reader = new BufferedReader(new FileReader(root_sd+"/ceremony-assistant/"+filePath));
            xpp.setInput(reader);

            int eventType = xpp.getEventType();
            String tagname = xpp.getName();

            while ( !"requirements".equalsIgnoreCase(tagname) && eventType != XmlPullParser.END_DOCUMENT) {

                //TODO refactoring mais elegante fazer loop até END_TAG
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        curText = xpp.getText();
                        Log.v("bla", curText);
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("name")) {
                            cerimony.setCName(curText);
                        } else if (tagname.equalsIgnoreCase("creation_date")) {
                            cerimony.setCreationDate(curText);
                        } else if (tagname.equalsIgnoreCase("initial_date")) {
                            cerimony.setInitialDate(curText);
                        } else if (tagname.equalsIgnoreCase("final_date")) {
                            cerimony.setFinalDate(curText);
                        } else if (tagname.equalsIgnoreCase("local")) {
                            cerimony.setLocal(curText);
                        } else if (tagname.equalsIgnoreCase("short_name")) {
                            cerimony.setShortName(curText);
                        }
                        break;
                    default:
                        break;
                }

                eventType = xpp.next();
                tagname = xpp.getName();
            }

            List<String> requirements = new ArrayList<String>();
            while ( !"steps".equalsIgnoreCase(tagname) && eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        curText = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("item"))
                            requirements.add(curText);
                            break;
                    default:
                        break;
                }
                eventType = xpp.next();
                tagname = xpp.getName();
            }
            cerimony.setRequirements(requirements);
            eventType = xpp.next();
            tagname = xpp.getName();

            List<Step> steps = new ArrayList<Step>();
            while(!("steps".equalsIgnoreCase(tagname) && eventType == XmlPullParser.END_TAG)) {

                if ("step".equalsIgnoreCase(tagname) && eventType == XmlPullParser.START_TAG) {
                    Step curStep = new Step();
                    String stepName = xpp.getAttributeValue(null, "name");
                    curStep.setSName(stepName);

                    while (!("step".equalsIgnoreCase(tagname) && eventType == XmlPullParser.END_TAG)) {

                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                break;
                            case XmlPullParser.TEXT:
                                curText = xpp.getText();
                                break;
                            case XmlPullParser.END_TAG:
                                if (tagname.equalsIgnoreCase("input")) {
                                    curStep.setInput(curText);
                                } else if (tagname.equalsIgnoreCase("output")) {
                                    curStep.setOutput(curText);
                                } else if (tagname.equalsIgnoreCase("description")) {
                                    curStep.setDescription(curText);
                                } else if (tagname.equalsIgnoreCase("observation")) {
                                    curStep.setObservation(curText);
                                }
                                break;
                            default:
                                break;

                        }
                        eventType = xpp.next();
                        tagname = xpp.getName();
                    }

                    steps.add(curStep);
                }
                eventType = xpp.next();
                tagname = xpp.getName();
            }
            cerimony.setSteps(steps);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cerimony;
    }
}
