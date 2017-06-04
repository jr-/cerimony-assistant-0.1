package tcc.cerimony_assistant_v01;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
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

            BufferedReader reader = new BufferedReader(new InputStreamReader(ctx.getAssets().open(filePath)));

            xpp.setInput(reader);

            int eventType = xpp.getEventType();
            String tagname = xpp.getName();

            while ( !"participant".equalsIgnoreCase(tagname) && eventType != XmlPullParser.END_DOCUMENT) {

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
                        }
                        break;
                    default:
                        break;
                }

                eventType = xpp.next();
                tagname = xpp.getName();
            }
            List<Participant> participants = new ArrayList<Participant>();
            while ( !"steps".equalsIgnoreCase(tagname) && eventType != XmlPullParser.END_DOCUMENT) {

                if("participant".equalsIgnoreCase(tagname) && eventType == XmlPullParser.START_TAG) {
                    Participant curParticipant = new Participant();
                    eventType = xpp.next();
                    tagname = xpp.getName();
                    while (!"participant".equalsIgnoreCase(tagname)) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                break;
                            case XmlPullParser.TEXT:
                                curText = xpp.getText();
                                Log.v("bla", curText);
                                break;
                            case XmlPullParser.END_TAG:
                                if(tagname.equalsIgnoreCase("name")) {
                                    curParticipant.setPName(curText);
                                } else if(tagname.equalsIgnoreCase("cargo")) {
                                    curParticipant.setCargo(curText);
                                } else if(tagname.equalsIgnoreCase("unidade")) {
                                    curParticipant.setUnidade(curText);
                                } else if(tagname.equalsIgnoreCase("email")) {
                                    curParticipant.setEmail(curText);
                                }
                                break;
                            default:
                                break;

                        }
                        eventType = xpp.next();
                        tagname = xpp.getName();
                    }
                    participants.add(curParticipant);

                }
                eventType = xpp.next();
                tagname = xpp.getName();

            }
            cerimony.setParticipants(participants);


            eventType = xpp.next();
            tagname = xpp.getName();


            List<Step> steps = new ArrayList<Step>();
            while(!("steps".equalsIgnoreCase(tagname) && eventType == XmlPullParser.END_TAG)) {

                if ("step".equalsIgnoreCase(tagname) && eventType == XmlPullParser.START_TAG) {
                    Step curStep = new Step();
                    while (!("step".equalsIgnoreCase(tagname) && eventType == XmlPullParser.END_TAG)) {

                        if ("inputs".equalsIgnoreCase(tagname) && eventType == XmlPullParser.START_TAG) {
                            List<String> inputs = new ArrayList<String>();
                            eventType = xpp.next();
                            tagname = xpp.getName();
                            while (!("inputs".equalsIgnoreCase(tagname) && eventType == XmlPullParser.END_TAG)) {
                                switch (eventType) {
                                    case XmlPullParser.START_TAG:
                                        break;
                                    case XmlPullParser.TEXT:
                                        curText = xpp.getText();
                                        break;
                                    case XmlPullParser.END_TAG:
                                        if (tagname.equalsIgnoreCase("input")) {
                                            inputs.add(curText);
                                        }
                                        break;
                                    default:
                                        break;

                                }
                                eventType = xpp.next();
                                tagname = xpp.getName();

                            }
                            curStep.setInput(inputs);
                        }

                        if ("outputs".equalsIgnoreCase(tagname) && eventType == XmlPullParser.START_TAG) {
                            List<String> outputs = new ArrayList<String>();
                            eventType = xpp.next();
                            tagname = xpp.getName();
                            while (!("outputs".equalsIgnoreCase(tagname) && eventType == XmlPullParser.END_TAG)) {
                                switch (eventType) {
                                    case XmlPullParser.START_TAG:
                                        break;
                                    case XmlPullParser.TEXT:
                                        curText = xpp.getText();
                                        break;
                                    case XmlPullParser.END_TAG:
                                        if (tagname.equalsIgnoreCase("output")) {
                                            outputs.add(curText);
                                        }
                                        break;
                                    default:
                                        break;

                                }
                                eventType = xpp.next();
                                tagname = xpp.getName();

                            }
                            curStep.setOutput(outputs);
                        }

                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                break;
                            case XmlPullParser.TEXT:
                                curText = xpp.getText();
                                break;
                            case XmlPullParser.END_TAG:
                                if (tagname.equalsIgnoreCase("description")) {
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
