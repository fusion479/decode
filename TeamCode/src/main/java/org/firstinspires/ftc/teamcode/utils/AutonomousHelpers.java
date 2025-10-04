package org.firstinspires.ftc.teamcode.utils;

import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;

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
        Point startPoint = new Point(startPose.getX(), startPose.getY());
        Point endPoint = new Point(endPose.getX(), endPose.getY());

        Path path = new Path(new BezierLine(startPoint, endPoint));
        setHeadingInterpolation(path, startPose.getHeading(), endPose.getHeading(), interpolation);

        return path;
    }

    public static Path buildCurve(Pose startPose, Point controlPoint, Pose endPose,
                                  HeadingInterpolation interpolation) {
        Point startPoint = new Point(startPose.getX(), startPose.getY());
        Point endPoint = new Point(endPose.getX(), endPose.getY());

        Path path = new Path(new BezierCurve(startPoint, controlPoint, endPoint));
        setHeadingInterpolation(path, startPose.getHeading(), endPose.getHeading(), interpolation);

        return path;
    }

    public static Path buildCurve(Pose startPose, Point firstControlPoint, Point secondControlPoint,
                                  Pose endPose, HeadingInterpolation interpolation) {
        Point startPoint = new Point(startPose.getX(), startPose.getY());
        Point endPoint = new Point(endPose.getX(), endPose.getY());

        Path path = new Path(new BezierCurve(startPoint, firstControlPoint, secondControlPoint, endPoint));
        setHeadingInterpolation(path, startPose.getHeading(), endPose.getHeading(), interpolation);

        return path;
    }

    public static Path buildCurve(Pose startPose, Point firstControlPoint, Point secondControlPoint,
                                  Point thirdControlPoint, Pose endPose, HeadingInterpolation interpolation) {
        Point startPoint = new Point(startPose.getX(), startPose.getY());
        Point endPoint = new Point(endPose.getX(), endPose.getY());

        Path path = new Path(new BezierCurve(startPoint, firstControlPoint, secondControlPoint, thirdControlPoint, endPoint));
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
                    Math.toRadians(lines.getJSONObject(0).getJSONObject("endPoint").getDouble("startDeg"))
            ));

            for (int i = 0; i < lines.length(); i++) {
                JSONObject line = lines.getJSONObject(i);
                poses.put(line.getJSONObject("endPoint").getString("name"), getPose(line.getJSONObject("endPoint")));

                JSONArray controlPoints = line.getJSONArray("controlPoints");
                for (int j = 0; j < controlPoints.length(); j++) {
                    poses.put(controlPoints.getJSONObject(j).getString("name"), getPose(controlPoints.getJSONObject(j)));
                }
            }

            return poses;
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<Pose> getPoses(String path) {
        try {
            ArrayList<Pose> poses = new ArrayList<Pose>();
            String jsonString = "";

            File file = new File(path);
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine())
                jsonString += reader.nextLine();

            JSONObject data = new JSONObject(jsonString);
            JSONArray lines = data.getJSONArray("lines");

            poses.add(new Pose(
                    data.getJSONObject("startPoint").getDouble("x"),
                    data.getJSONObject("startPoint").getDouble("y"),
                    Math.toRadians(lines.getJSONObject(0).getJSONObject("endPoint").getDouble("startDeg"))
            ));

            for (int i = 0; i < lines.length(); i++) {
                JSONObject line = lines.getJSONObject(i);
                System.out.println(line);
                poses.add(getPose(line.getJSONObject("endPoint")));

                JSONArray controlPoints = line.getJSONArray("controlPoints");
                for (int j = 0; j < controlPoints.length(); j++) {
                    poses.add(getPose(controlPoints.getJSONObject(j)));
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
                object.has("startDeg") ? Math.toRadians(object.getDouble("startDeg")) : -1.0
        );
    }

    public static Point poseToPoint(Pose pose) {
        return new Point(pose.getX(), pose.getY());
    }

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