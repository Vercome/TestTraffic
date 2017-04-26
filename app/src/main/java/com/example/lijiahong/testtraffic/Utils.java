package com.example.lijiahong.testtraffic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/4/25.
 */

public class Utils {
    private final static String GET_CAR_SPEED_PATH="GetCarSpeed.do";
    private final static String SET_CAR_MOVE_PATH="SetCarMove.do";
    private final static String GET_CAR_ACCOUNT_BALANCE_PATH="GetCarAccountBalance.do";
    private final static String SET_CAR_ACCOUNT_RECHARGE_PATH="SetCarAccountRecharge.do";
    private final static String GET_TRAFFIC_LIGHT_CONFIG_ACTION_PATH="GetTrafficLightConfigAction.do";
    private final static String SET_PARK_RATE_PATH="SetParkRate.do";
    private final static String GET_PARK_RATE_PATH="GetParkRate.do";
    private final static String GET_PARK_FREE_PATH="GetParkFree.do";
    private final static String GET_ALL_SENSE_PATH="GetAllSense.do";
    private final static String GET_LIGHT_SENSE_VALVE_PATH="GetLightSenseValve.do";
    private final static String GET_BUSSTATION_INFO_PATH="GetBusStationInfo.do";
    private final static String GET_ROAD_STATUS_PATH="GetRoadStatus.do";
    final static int GET_CAR_SPEED=0;
    final static int SET_CAR_MOVE=1;
    final static int GET_CAR_ACCOUNT_BALANCE=2;
    final static int SET_CAR_ACCOUNT_RECHARGE=3;
    final static int GET_TRAFFIC_LIGHT_CONFIG_ACTION=4;
    final static int SET_PARK_RATE=5;
    final static int GET_PARK_RATE=6;
    final static int GET_ALL_SENSE=7;
    final static int GET_PARK_FREE=8;
    final static int GET_LIGHT_SENSE_VALVE=9;
    final static int GET_BUSSTATION_INFO=10;
    final static int GET_ROAD_STATUS=11;
    private Context context;
    String ip;
    public void setIp(String ip){
        SharedPreferences sp=context.getSharedPreferences("IP",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("IP",ip);
        this.ip=ip;
    }
    public Utils(Context context){
        this.context=context;
    }
    public String getConnection(String path, JSONObject jsonObject){
        try {
            URL url=new URL(path);
            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setConnectTimeout(8000);
            urlConnection.connect();
            PrintWriter pw=new PrintWriter(urlConnection.getOutputStream());
            pw.write(jsonObject.toString());
            BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String serverinfo=br.readLine();
            JSONObject infoJson=new JSONObject(serverinfo);
            String result=infoJson.getString("serverinfo");
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "未获得值";
    }
    public void getResult(Handler handler,int type,Object param1,Object param2){
        JSONObject jsonObject=new JSONObject();
        String post="http://"+ip+":8080/transportservice/type/jason/action/";
        String result="";
        try {
            switch (type){
                case Utils.GET_CAR_SPEED:
                    jsonObject.put("CarId",param1);
                    result=getConnection(post+GET_CAR_SPEED_PATH,jsonObject);
                    break;
                case Utils.SET_CAR_MOVE:
                    jsonObject.put("CarId",param1);
                    jsonObject.put("CarAction",param2);
                    result=getConnection(post+SET_CAR_MOVE_PATH,jsonObject);
                    break;
                case Utils.GET_CAR_ACCOUNT_BALANCE:
                    jsonObject.put("CarId",param1);
                    result=getConnection(post+GET_CAR_ACCOUNT_BALANCE_PATH,jsonObject);
                    break;
                case Utils.SET_CAR_ACCOUNT_RECHARGE:
                    jsonObject.put("CarId",param1);
                    jsonObject.put("Money",param2);
                    result=getConnection(post+SET_CAR_ACCOUNT_RECHARGE_PATH,jsonObject);
                    break;
                case Utils.GET_TRAFFIC_LIGHT_CONFIG_ACTION:
                    jsonObject.put("TrafficLightId",param1);
                    result=getConnection(post+GET_TRAFFIC_LIGHT_CONFIG_ACTION_PATH,jsonObject);
                    break;
                case Utils.SET_PARK_RATE:
                    jsonObject.put("RateType",param1);
                    jsonObject.put("Money",param2);
                    result=getConnection(post+SET_PARK_RATE_PATH,jsonObject);
                    break;
                case Utils.GET_PARK_RATE:
                    result=getConnection(post+GET_PARK_RATE_PATH,null);
                    break;
                case Utils.GET_PARK_FREE:
                    result=getConnection(post+GET_PARK_FREE_PATH,null);
                    break;
                case Utils.GET_ALL_SENSE:
                    result=getConnection(post+GET_ALL_SENSE_PATH,null);
                    break;
                case Utils.GET_LIGHT_SENSE_VALVE:
                    result=getConnection(post+GET_LIGHT_SENSE_VALVE_PATH,null);
                    break;
                case Utils.GET_BUSSTATION_INFO:
                    jsonObject.put("BusStationId",param1);
                    result=getConnection(post+GET_BUSSTATION_INFO_PATH,jsonObject);
                    break;
                case Utils.GET_ROAD_STATUS:
                    jsonObject.put("RoadId",param1);
                    result=getConnection(post+GET_ROAD_STATUS_PATH,jsonObject);
                    break;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Message message=new Message();
        message.obj=result;
        message.what=type;
        handler.sendMessage(message);
    }
}
