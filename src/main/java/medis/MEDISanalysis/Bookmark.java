package medis.MEDISanalysis;

public class Bookmark {
    int id;
    long timeStamp;
    int timePos;
    String deviceType;

    Bookmark(int _id, long _timeStamp, int _timePos, String _deviceType){
        id = _id;
        timeStamp = _timeStamp;
        timePos = _timePos;
        deviceType = _deviceType;
    }

    public int getID(){
        return id;
    }
    public long getTimeStamp(){
        return timeStamp;
    }

    public int getTimePos(){
        return timePos;
    }


    public String getDeviceType(){
        return deviceType;
    }
}
