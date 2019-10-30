package mobi.acpm.inspeckage.mymonitor;


import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class AppMonitorService extends BaseAccessibilityService {
    public static String TAG = "Inspeckage_AccessibilityService";



    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        //Log.d(TAG, "packageName -> " + packageName);
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        goThrough(rootNode);
    }


    private boolean click_by_text(AccessibilityNodeInfo nodeInfo, String text) {
        List<AccessibilityNodeInfo> nodes = findNodesByText(nodeInfo, text);
        if (nodes != null){
            for (AccessibilityNodeInfo node : nodes) {
                if (node.isClickable()) {
                    Log.d(TAG, "click -> " + text);
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean click_by_id(AccessibilityNodeInfo nodeInfo, String id) {
        List<AccessibilityNodeInfo> nodes = findNodesById(nodeInfo, id);
        if (nodes != null){
            for (AccessibilityNodeInfo node : nodes) {
                if (node.isClickable()) {
                    Log.d(TAG, "click -> " + id);
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            }
        }
        return false;
    }

    public void goThrough(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            click_by_text(nodeInfo, "允许");
        }
    }

    @Override
    protected void onServiceConnected() {
        Log.d(TAG, "onServiceConnected");
        //stage = stage_open_network_settings;
        super.onServiceConnected();
    }

    private List<AccessibilityNodeInfo> findNodesByText(AccessibilityNodeInfo nodeInfo, String text){
        return nodeInfo.findAccessibilityNodeInfosByText(text);
    }

    private List<AccessibilityNodeInfo> findNodesById(AccessibilityNodeInfo nodeInfo, String id){
        return nodeInfo.findAccessibilityNodeInfosByViewId(id);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

//    private void NotifyUnbind(){
//        Log.d(TAG, "NotifyUnbind");
//        Intent intent = new Intent(UnbindDeviceReceiver.ACTION_UNBIND);
//        intent.putExtra("status", "NO");
//        sendBroadcast(intent);
//    }
}
