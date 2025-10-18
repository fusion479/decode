package org.firstinspires.ftc.teamcode.utils;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class AutonomousHelpers {

    public enum HeadingInterpolation {
        LINEAR,
        CONSTANT,
        TANGENT
    }

    public static Path buildLine(Pose startPose, Pose endPose, HeadingInterpolation interpolation) {
        Path path = new Path(new BezierLine(startPose, endPose));
        setHeadingInterpolation(path, startPose.getHeading(), endPose.getHeading(), interpolation);

        return path;
    }

    public static Path buildCurve(Pose startPose, Pose controlPose, Pose endPose,
                                  HeadingInterpolation interpolation) {
        Path path = new Path(new BezierCurve(startPose, controlPose, endPose));
        setHeadingInterpolation(path, startPose.getHeading(), endPose.getHeading(), interpolation);

        return path;
    }

    public static Path buildCurve(Pose startPose, Pose firstControlPose, Pose secondControlPose,
                                  Pose endPose, HeadingInterpolation interpolation) {
        Path path = new Path(new BezierCurve(startPose, firstControlPose, secondControlPose, endPose));
        setHeadingInterpolation(path, startPose.getHeading(), endPose.getHeading(), interpolation);

        return path;
    }

    public static Path buildCurve(Pose startPose, Pose firstControlPose, Pose secondControlPose,
                                  Pose thirdControlPose, Pose endPose, HeadingInterpolation interpolation) {
        Path path = new Path(new BezierCurve(startPose, firstControlPose, secondControlPose, thirdControlPose, endPose));
        setHeadingInterpolation(path, startPose.getHeading(), endPose.getHeading(), interpolation);

        return path;
    }

    public static HashMap<String, Pose> getPosesByName(String path) {
        try {
            HashMap<String, Pose> poses = new HashMap<String, Pose>();
            String jsonString = "";

            File file = new File(path);
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine())
                jsonString += reader.nextLine();

            JSONObject data = new JSONObject(jsonString);
            JSONArray lines = data.getJSONArray("lines");

            poses.put("startPoint", new Pose(
                    data.getJSONObject("startPoint").getDouble("x"),
                    data.getJSONObject("startPoint").getDouble("y"),
                    Math.toRadians(data.getJSONObject("startPoint").getDouble("startDeg"))
            ));

            for (int i = 0; i < lines.length(); i++) {
                JSONObject line = lines.getJSONObject(i);
                poses.put(line.getString("name"), getPose(line.getJSONObject("endPoint")));

                JSONArray controlPoints = line.getJSONArray("controlPoints");
                for (int j = 0; j < controlPoints.length(); j++) {
                    poses.put(line.getString("name") + "Control" + j, getPose(controlPoints.getJSONObject(j)));
                }
            }

            return poses;
        } catch (Exception e) {
            return null;
        }
    }

    private static Pose getPose(JSONObject object) throws JSONException {
        return new Pose(
                object.getDouble("x"),
                object.getDouble("y"),
                object.has("endDeg") ? Math.toRadians(object.getDouble("endDeg")) : -1.0
        );
    }

//    public static ArrayList<Pose> getPoses(String path) {
//        try {
//            ArrayList<Pose> poses = new ArrayList<Pose>();
//            String jsonString = "";
//
//            File file = new File(path);
//            Scanner reader = new Scanner(file);
//
//            while (reader.hasNextLine())
//                jsonString += reader.nextLine();
//
//            JSONObject data = new JSONObject(jsonString);
//            JSONArray lines = data.getJSONArray("lines");
//
//            poses.add(new Pose(
//                    data.getJSONObject("startPoint").getDouble("x"),
//                    data.getJSONObject("startPoint").getDouble("y"),
//                    Math.toRadians(lines.getJSONObject(0).getJSONObject("endPoint").getDouble("startDeg"))
//            ));
//
//            for (int i = 0; i < lines.length(); i++) {
//                JSONObject line = lines.getJSONObject(i);
//                System.out.println(line);
//                poses.add(getPose(line.getJSONObject("endPoint")));
//
//                JSONArray controlPoints = line.getJSONArray("controlPoints");
//                for (int j = 0; j < controlPoints.length(); j++) {
//                    poses.add(getPose(controlPoints.getJSONObject(j)));
//                }
//            }
//
//            return poses;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    private static void setHeadingInterpolation(Path path, double startHeading, double endHeading,
                                                HeadingInterpolation interpolation) {
        switch (interpolation) {
            case LINEAR:
                path.setLinearHeadingInterpolation(startHeading, endHeading);
                break;
            case CONSTANT:
                path.setConstantHeadingInterpolation(startHeading);
                break;
            case TANGENT:
                path.setTangentHeadingInterpolation();
                break;
        }
    }
}