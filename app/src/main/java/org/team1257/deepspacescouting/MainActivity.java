package org.team1257.deepspacescouting;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import org.apache.commons.lang3.StringUtils;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.net.URLEncoder;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private String settingsSpinner;
    private String objectiveEvent;
    private String objectiveTeam; //needs to be updated
    private String objectiveMatch; //needs to be updated
    private String objectiveInitials; //needs to be updated
    private String objectiveSandstormStart;
    private String objectiveSandstormPreload;
    private String objectiveSandstormHatch; //needs to be updated
    private String objectiveSandstormCargo; //needs to be updated
    private int[] objectiveHatch = new int[5];
    private int[] objectiveCargo = new int[5];
    private String objectiveEndgameClimb;
    private String objectiveEndgameClimbHelp;
    private String objectiveNotes; //needs to be updated
    private String objectiveDate; //needs to be updated
    private String pitEvent;
    private String pitTeam; //needs to be updated
    private String pitRole; //needs to be updated
    private String pitInitials; //needs to be updated
    private String pitSandstormInformation;
    private boolean pitSandstormFrontHatch; //needs to be updated
    private String pitRobotSpeed; //needs to be updated
    private String pitRobotWeight; //needs to be updated
    private String pitRobotVision;
    private String pitRobotDrivetrain; //needs to be updated
    private String pitRobotHatchMechanism; //needs to be updated
    private String pitRobotCargoMechanism; //needs to be updated
    private String pitClimbMechanism; //needs to be updated
    private String pitClimbHelpMechanism; //needs to be updated
    private String pitDriveTeamExperience; //needs to be updated
    private String pitNotes; //needs to be updated
    private String pitDate; //needs to be updated
    private String sendFileSpinner;
    private String sendFileSource;
    private String sendText;
    private static final UUID sendUUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ConnectedThread mConnectedThread;
    public void navigate(android.view.View view) {
        int[] scrollViewIDs = {R.id.instructionsLayout, R.id.objectiveLayout, R.id.pitLayout, R.id.sendLayout, R.id.settingsLayout};
        int[] scrollViewLinkIDs = {R.id.instructionsLayoutLink, R.id.objectiveLayoutLink, R.id.pitLayoutLink, R.id.sendLayoutLink, R.id.settingsLayoutLink};
        findViewById(R.id.instructionsLayout).setVisibility(View.GONE);
        findViewById(R.id.objectiveLayout).setVisibility(View.GONE);
        findViewById(R.id.pitLayout).setVisibility(View.GONE);
        findViewById(R.id.sendLayout).setVisibility(View.GONE);
        findViewById(R.id.settingsLayout).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.instructionsLayoutLink)).setTextColor(getResources().getColor(R.color.text_color_primary));
        ((TextView) findViewById(R.id.objectiveLayoutLink)).setTextColor(getResources().getColor(R.color.text_color_primary));
        ((TextView) findViewById(R.id.pitLayoutLink)).setTextColor(getResources().getColor(R.color.text_color_primary));
        ((TextView) findViewById(R.id.sendLayoutLink)).setTextColor(getResources().getColor(R.color.text_color_primary));
        ((TextView) findViewById(R.id.settingsLayoutLink)).setTextColor(getResources().getColor(R.color.text_color_primary));
        int visibleID = scrollViewIDs[linearSearch(scrollViewLinkIDs, view.getId())];
        findViewById(visibleID).setVisibility(View.VISIBLE);
        ((TextView) view).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }
    private int linearSearch(int[] arr, int key) {
        int res = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == key) {
                res = i;
                break;
            }
        }
        return res;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        normalizeSpinners();
        try {
            File path = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "2019ScoutingLogs"), "settingsLogs");
            if (!(path.exists())) {
                path.mkdir();
            }
            File log = new File(path, "spinner.txt");
            String stringUnmodified = (new BufferedReader(new FileReader(log))).readLine();
            int index = 0;
            if (StringUtils.isNumeric(stringUnmodified)) {
                index = Math.min(Integer.parseInt(stringUnmodified), 5);
            }
            ((Spinner) findViewById(R.id.settingsSpinners)).setSelection(index);
            settingsSpinner = (String) ((Spinner) findViewById(R.id.settingsSpinners)).getItemAtPosition(index);
            String str = "Scout this robot: " + settingsSpinner;
            ((TextView) findViewById(R.id.objectiveScoutNotify)).setText(str);
            File olog = new File(path, "OID.txt");
            String ostringUnmodified = (new BufferedReader(new FileReader(olog))).readLine();
            ((EditText) findViewById(R.id.settingsOID)).setText(ostringUnmodified);
            File plog = new File(path, "PID.txt");
            String pstringUnmodified = (new BufferedReader(new FileReader(plog))).readLine();
            ((EditText) findViewById(R.id.settingsOID)).setText(pstringUnmodified);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        View objectiveLayout = findViewById(R.id.objectiveLayout);
        View pitLayout = findViewById(R.id.pitLayout);
        setListeners(objectiveLayout);
        setListeners(pitLayout);
        try {
            File path = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "2019ScoutingLogs"), "settingsLogs");
            if (!(path.exists())) {
                path.mkdir();
            }
            File log = new File(path, "spinner.txt");

            String stringUnmodified = (new BufferedReader(new FileReader(log))).readLine();
            int index = 0;
            if (StringUtils.isNumeric(stringUnmodified)) {
                index = Math.min(Integer.parseInt(stringUnmodified), 5);
            }
            ((Spinner) findViewById(R.id.settingsSpinners)).setSelection(index);
            settingsSpinner = (String) ((Spinner) findViewById(R.id.settingsSpinners)).getItemAtPosition(index);
            String str = "Scout this robot: " + settingsSpinner;
            ((TextView) findViewById(R.id.objectiveScoutNotify)).setText(str);
            File olog = new File(path, "OID.txt");
            String ostringUnmodified = (new BufferedReader(new FileReader(olog))).readLine();
            ((EditText) findViewById(R.id.settingsOID)).setText(ostringUnmodified);
            File plog = new File(path, "PID.txt");
            String pstringUnmodified = (new BufferedReader(new FileReader(plog))).readLine();
            ((EditText) findViewById(R.id.settingsOID)).setText(pstringUnmodified);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void normalizeSpinners() {
        final Spinner settingsSpinners = (Spinner) findViewById(R.id.settingsSpinners);
        String[] settingsSpinnersArray = {"Nearest Red", "Middle Red", "Farthest Red", "Nearest Blue", "Middle Blue", "Farthest Blue"};
        List<String> settingsSpinnersList = new ArrayList<>(Arrays.asList(settingsSpinnersArray));
        ArrayAdapter<String> settingsSpinnersArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, settingsSpinnersList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        settingsSpinnersArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        settingsSpinners.setAdapter(settingsSpinnersArrayAdapter);
        settingsSpinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingsSpinner = (String) parent.getItemAtPosition(position);
                String str = "Scout this robot: " + settingsSpinner;
                ((TextView) findViewById(R.id.objectiveScoutNotify)).setText(str);
                try {
                    File path = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "2019ScoutingLogs"), "settingsLogs");
                    if (!(path.exists())) {
                        path.mkdir();
                    }
                    File log = new File(path, "spinner.txt");
                    FileOutputStream out = new FileOutputStream(log, false);
                    OutputStreamWriter writer = new OutputStreamWriter(out);
                    writer.write(Integer.toString(settingsSpinners.getSelectedItemPosition()));
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Spinner sendFileSpinners = (Spinner) findViewById(R.id.sendFileSpinners);
        String[] sendFileSpinnersArray = {"Choose one...", "Error Log", "Complete Log"};
        List<String> sendFileSpinnersList = new ArrayList<>(Arrays.asList(sendFileSpinnersArray));
        ArrayAdapter<String> sendFileSpinnersArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, sendFileSpinnersList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        sendFileSpinnersArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        sendFileSpinners.setAdapter(sendFileSpinnersArrayAdapter);
        sendFileSpinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendFileSpinner = (String) parent.getItemAtPosition(position);
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
        });
        Spinner sendFileSources = (Spinner) findViewById(R.id.sendFileSources);
        String[] sendFileSourcesArray = {"Choose one...", "Objective", "Pit"};
        List<String> sendFileSourcesList = new ArrayList<>(Arrays.asList(sendFileSourcesArray));
        ArrayAdapter<String> sendFileSourcesArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, sendFileSourcesList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        sendFileSourcesArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        sendFileSources.setAdapter(sendFileSourcesArrayAdapter);
        sendFileSources.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendFileSource = (String) parent.getItemAtPosition(position);
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
        });
        final Spinner sendPairs = (Spinner) findViewById(R.id.sendPairs);
        String[] sendPairsArray = {"Choose one...", "A", "B", "C", "D", "E", "F"};
        List<String> sendPairsList = new ArrayList<>(Arrays.asList(sendPairsArray));
        ArrayAdapter<String> sendPairsArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, sendPairsList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        sendPairsArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        sendPairs.setAdapter(sendPairsArrayAdapter);
        sendPairs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pairDevice(sendPairs, Math.max(0, position - 1));
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
        });
        Spinner objectiveEvents = (Spinner) findViewById(R.id.objectiveEvents);
        String[] objectiveEventsArray = {"Choose one...", "Training", "FMA DCMP"};
        List<String> objectiveEventsList = new ArrayList<>(Arrays.asList(objectiveEventsArray));
        ArrayAdapter<String> objectiveEventsArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, objectiveEventsList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        objectiveEventsArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        objectiveEvents.setAdapter(objectiveEventsArrayAdapter);
        objectiveEvents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objectiveEvent = (String) parent.getItemAtPosition(position);
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
        });
        Spinner objectiveSandstormStarts = (Spinner) findViewById(R.id.objectiveSandstormStarts);
        String[] objectiveSandstormStartsArray = {"Choose one...", "None", "Level 1", "Level 2"};
        List<String> objectiveSandstormStartsList = new ArrayList<>(Arrays.asList(objectiveSandstormStartsArray));
        ArrayAdapter<String> objectiveSandstormStartsArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, objectiveSandstormStartsList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        objectiveSandstormStartsArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        objectiveSandstormStarts.setAdapter(objectiveSandstormStartsArrayAdapter);
        objectiveSandstormStarts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objectiveSandstormStart = (String) parent.getItemAtPosition(position);
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
        });
        Spinner objectiveSandstormPreloads = (Spinner) findViewById(R.id.objectiveSandstormPreloads);
        String[] objectiveSandstormPreloadsArray = {"Choose one...", "None", "Cargo", "Hatch"};
        List<String> objectiveSandstormPreloadsList = new ArrayList<>(Arrays.asList(objectiveSandstormPreloadsArray));
        ArrayAdapter<String> objectiveSandstormPreloadsArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, objectiveSandstormPreloadsList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        objectiveSandstormPreloadsArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        objectiveSandstormPreloads.setAdapter(objectiveSandstormPreloadsArrayAdapter);
        objectiveSandstormPreloads.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objectiveSandstormPreload = (String) parent.getItemAtPosition(position);
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
        });
        final Spinner objectiveEndgameClimbs = (Spinner) findViewById(R.id.objectiveEndgameClimbs);
        String[] objectiveEndgameClimbsArray = {"Choose one...", "None", "Level 1", "Level 2", "Level 3"};
        List<String> objectiveEndgameClimbsList = new ArrayList<>(Arrays.asList(objectiveEndgameClimbsArray));
        ArrayAdapter<String> objectiveEndgameClimbsArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, objectiveEndgameClimbsList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        objectiveEndgameClimbsArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        objectiveEndgameClimbs.setAdapter(objectiveEndgameClimbsArrayAdapter);
        objectiveEndgameClimbs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objectiveEndgameClimb = (String) parent.getItemAtPosition(position);
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
        });
        final Spinner objectiveEndgameClimbHelps = (Spinner) findViewById(R.id.objectiveEndgameClimbHelps);
        String[] objectiveEndgameClimbHelpsArray = {"Choose one...", "None", "Received", "L2", "L3", "L2/L2", "L2/L3", "L3/L3"};
        List<String> objectiveEndgameClimbHelpsList = new ArrayList<>(Arrays.asList(objectiveEndgameClimbHelpsArray));
        ArrayAdapter<String> objectiveEndgameClimbHelpsArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, objectiveEndgameClimbHelpsList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        objectiveEndgameClimbHelpsArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        objectiveEndgameClimbHelps.setAdapter(objectiveEndgameClimbHelpsArrayAdapter);
        objectiveEndgameClimbHelps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objectiveEndgameClimbHelp = (String) parent.getItemAtPosition(position);
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
            }
        });
        Spinner pitEvents = (Spinner) findViewById(R.id.pitEvents);
        String[] pitEventsArray = {"Choose one...", "Training", "Mount Olive", "Bridgewater-Raritan"};
        List<String> pitEventsList = new ArrayList<>(Arrays.asList(pitEventsArray));
        ArrayAdapter<String> pitEventsArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, pitEventsList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        pitEventsArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        pitEvents.setAdapter(pitEventsArrayAdapter);
        pitEvents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pitEvent = (String) parent.getItemAtPosition(position);
                findViewById(R.id.pitResetValidator).setVisibility(View.GONE);
                findViewById(R.id.pitReset).setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                findViewById(R.id.pitResetValidator).setVisibility(View.GONE);
                findViewById(R.id.pitReset).setVisibility(View.VISIBLE);
            }
        });
        Spinner pitSandstormInformations = (Spinner) findViewById(R.id.pitSandstormInformations);
        String[] pitSandstormInformationsArray = {"Choose one...", "Autonomous", "Camera Feed", "Blind"};
        List<String> pitSandstormInformationsList = new ArrayList<>(Arrays.asList(pitSandstormInformationsArray));
        ArrayAdapter<String> pitSandstormInformationsArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, pitSandstormInformationsList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        pitSandstormInformationsArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        pitSandstormInformations.setAdapter(pitSandstormInformationsArrayAdapter);
        pitSandstormInformations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pitSandstormInformation = (String) parent.getItemAtPosition(position);
                findViewById(R.id.pitResetValidator).setVisibility(View.GONE);
                findViewById(R.id.pitReset).setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                findViewById(R.id.pitResetValidator).setVisibility(View.GONE);
                findViewById(R.id.pitReset).setVisibility(View.VISIBLE);
            }
        });
        Spinner pitRobotVisions = (Spinner) findViewById(R.id.pitRobotVisions);
        String[] pitRobotVisionsArray = {"Choose one...", "Yes", "No"};
        List<String> pitRobotVisionsList = new ArrayList<>(Arrays.asList(pitRobotVisionsArray));
        ArrayAdapter<String> pitRobotVisionsArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, pitRobotVisionsList) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };
        pitRobotVisionsArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        pitRobotVisions.setAdapter(pitRobotVisionsArrayAdapter);
        pitRobotVisions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pitRobotVision = (String) parent.getItemAtPosition(position);
                findViewById(R.id.pitResetValidator).setVisibility(View.GONE);
                findViewById(R.id.pitReset).setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                findViewById(R.id.pitResetValidator).setVisibility(View.GONE);
                findViewById(R.id.pitReset).setVisibility(View.VISIBLE);
            }
        });
    }
    public void setListeners(View layout) {
        if (layout instanceof Button) {
            if (((layout.getId() != R.id.objectiveSubmit) && (layout.getId() != R.id.objectiveReset)) && ((layout.getId() != R.id.pitSubmit) && (layout.getId() != R.id.pitReset))) {
                layout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Button confirm = (Button) findViewById(R.id.objectiveResetValidator);
                        boolean isView = v.equals(confirm);
                        if (isView) {
                            objectiveReset(findViewById(R.id.objectiveReset));
                        }
                        Button pconfirm = (Button) findViewById(R.id.pitResetValidator);
                        isView = v.equals(pconfirm);
                        if (isView) {
                            pitReset(findViewById(R.id.pitReset));
                        }
                        findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                        findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
                        findViewById(R.id.pitResetValidator).setVisibility(View.GONE);
                        findViewById(R.id.pitReset).setVisibility(View.VISIBLE);
                    }
                });
            }
        } else {
            layout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
                    findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
                    findViewById(R.id.pitResetValidator).setVisibility(View.GONE);
                    findViewById(R.id.pitReset).setVisibility(View.VISIBLE);
                }
            });
        }
        if (layout instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) layout).getChildCount(); i++) {
                setListeners(((ViewGroup) layout).getChildAt(i));
            }
        }
    }
    public void objectiveIndecrement(android.view.View view) {
        findViewById(R.id.objectiveResetValidator).setVisibility(View.GONE);
        findViewById(R.id.objectiveReset).setVisibility(View.VISIBLE);
        String tag = (String) view.getTag();
        int len = tag.length();
        int i = tag.charAt(len - 1) - 49;
        boolean positive = false;
        boolean hatch = false;
        switch (tag.charAt(len - 2)) {
            case '0': positive = true; break;
            case '-': positive = false; break;
        }
        switch (tag.charAt(len - 3)) {
            case 'C': hatch = false; break;
            case 'H': hatch = true; break;
        }
        View v;
        if (hatch) {
            v = ((ViewGroup) findViewById(R.id.objectiveHatches)).getChildAt(i);
            if (positive) {
                objectiveHatch[i]++;
            } else {
                if (objectiveHatch[i] > 0) {
                    objectiveHatch[i]--;
                }
            }
            ((TextView) v).setText(Integer.toString(objectiveHatch[i]));
        } else {
            v = ((ViewGroup) findViewById(R.id.objectiveCargos)).getChildAt(i);
            if (positive) {
                objectiveCargo[i]++;
            } else {
                if (objectiveCargo[i] > 0) {
                    objectiveCargo[i]--;
                }
            }
            ((TextView) v).setText(Integer.toString(objectiveCargo[i]));
        }
    }
    public void objectiveResetValidator(android.view.View view) {
        findViewById(R.id.objectiveResetValidator).setVisibility(View.VISIBLE);
        findViewById(R.id.objectiveReset).setVisibility(View.GONE);
    }
    public void pitResetValidator(android.view.View view) {
        findViewById(R.id.pitResetValidator).setVisibility(View.VISIBLE);
        findViewById(R.id.pitReset).setVisibility(View.GONE);
    }
    public void objectiveReset(android.view.View view) {
        submitValidator(findViewById(R.id.objectiveLayout), false);
        ((Spinner) findViewById(R.id.objectiveEvents)).setSelection(0);
        ((EditText) findViewById(R.id.objectiveTeam)).setText("");
        ((EditText) findViewById(R.id.objectiveMatch)).setText("");
        ((EditText) findViewById(R.id.objectiveInitials)).setText("");
        ((Spinner) findViewById(R.id.objectiveSandstormStarts)).setSelection(0);
        ((Spinner) findViewById(R.id.objectiveSandstormPreloads)).setSelection(0);
        ((EditText) findViewById(R.id.objectiveSandstormHatch)).setText("");
        ((EditText) findViewById(R.id.objectiveSandstormCargo)).setText("");
        ((Spinner) findViewById(R.id.objectiveEndgameClimbs)).setSelection(0);
        ((Spinner) findViewById(R.id.objectiveEndgameClimbHelps)).setSelection(0);
        ((TextInputEditText) findViewById(R.id.objectiveNotes)).setText("");
        ViewGroup h = (ViewGroup) findViewById(R.id.objectiveHatches);
        ViewGroup c = (ViewGroup) findViewById(R.id.objectiveCargos);
        for (int i = 0; i < 5; i++) {
            objectiveHatch[i] = 0;
            objectiveCargo[i] = 0;
            ((TextView) c.getChildAt(i)).setText("0");
            ((TextView) h.getChildAt(i)).setText("0");
        }
    }
    public void pitReset(android.view.View view) {
        submitValidator(findViewById(R.id.pitLayout), false);
        ((Spinner) findViewById(R.id.pitEvents)).setSelection(0);
        ((EditText) findViewById(R.id.pitTeam)).setText("");
        ((EditText) findViewById(R.id.pitRole)).setText("");
        ((EditText) findViewById(R.id.pitInitials)).setText("");
        ((Spinner) findViewById(R.id.pitSandstormInformations)).setSelection(0);
        if (((CheckBox) findViewById(R.id.pitSandstormFrontHatch)).isChecked()) {
            ((CheckBox) findViewById(R.id.pitSandstormFrontHatch)).toggle();
        }
        ((EditText) findViewById(R.id.pitRobotSpeed)).setText("");
        ((EditText) findViewById(R.id.pitRobotWeight)).setText("");
        ((Spinner) findViewById(R.id.pitRobotVisions)).setSelection(0);
        ((EditText) findViewById(R.id.pitRobotDrivetrain)).setText("");
        ((EditText) findViewById(R.id.pitRobotHatchMechanism)).setText("");
        ((EditText) findViewById(R.id.pitRobotCargoMechanism)).setText("");
        ((EditText) findViewById(R.id.pitClimbMechanism)).setText("");
        ((EditText) findViewById(R.id.pitClimbHelpMechanism)).setText("");
        ((EditText) findViewById(R.id.pitDriveTeamExperience)).setText("");
        ((TextInputEditText) findViewById(R.id.pitNotes)).setText("");
    }
    public void objectiveGetHTML(final String msgs, final boolean errorr) throws Exception {
        String msg = URLEncoder.encode(msgs, "UTF-8");
        String url = "https://docs.google.com/forms/d/e/" + ((EditText) findViewById(R.id.settingsOID)).getText() + "/formResponse?usp=pp_url&entry.615575561=" + msg + "&submit=Submit";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Successfully sent", Toast.LENGTH_SHORT).show();
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Sending error (see Harsh)", Toast.LENGTH_SHORT).show();
                if (errorr) {
                    try {
                        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "2019ScoutingLogs");
                        if (!(path.exists())) {
                            path.mkdir();
                        }
                        File log = new File(path, "objectiveErrorLog.txt");
                        FileOutputStream out = new FileOutputStream(log, true);
                        OutputStreamWriter writer = new OutputStreamWriter(out);
                        writer.append(msgs);
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Saving error (see Harsh)", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        queue.add(stringRequest);
    }
    public void pitGetHTML(final String msgs, final boolean errorr) throws Exception {
        String msg = URLEncoder.encode(msgs, "UTF-8");
        String url = "https://docs.google.com/forms/d/e/" + ((EditText) findViewById(R.id.settingsPID)).getText() + "/formResponse?entry.615575561=" + msg + "&submit=Submit";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Successfully sent", Toast.LENGTH_SHORT).show();
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Sending error (see Harsh)", Toast.LENGTH_SHORT).show();
                if (errorr) {
                    try {
                        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "2019ScoutingLogs");
                        if (!(path.exists())) {
                            path.mkdir();
                        }
                        File log = new File(path, "pitErrorLog.txt");
                        FileOutputStream out = new FileOutputStream(log, true);
                        OutputStreamWriter writer = new OutputStreamWriter(out);
                        writer.append(msgs);
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Saving error (see Harsh)", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        queue.add(stringRequest);
    }
    public boolean submitValidator(View layout, boolean submit) {
        boolean flag = false;
        if ((layout instanceof ViewGroup) && (!(layout instanceof Spinner))) {
            for (int i = 0; i < ((ViewGroup) layout).getChildCount(); i++) {
                flag = (submitValidator(((ViewGroup) layout).getChildAt(i), submit) || flag);
            }
        } else if (layout instanceof EditText) {
            String tag = (String) layout.getTag();
            String tagLabel = tag + "Label";
            TextView label = (TextView) findViewById(R.id.container).findViewWithTag(tagLabel);
            if ((((EditText) layout).getText().toString().equals("")) && submit) {
                flag = true;
                label.setTextColor(getResources().getColor(R.color.text_color_secondary));
                ((EditText) layout).setHintTextColor(getResources().getColor(R.color.text_color_secondary));
            } else {
                label.setTextColor(getResources().getColor(R.color.text_color_primary));
                ((EditText) layout).setHintTextColor(getResources().getColor(R.color.text_color_tertiary));
            }
        } else if (layout instanceof Spinner) {
            String tag = (String) layout.getTag();
            String tagLabel = tag + "Label";
            TextView label = (TextView) findViewById(R.id.container).findViewWithTag(tagLabel);
            if ((((Spinner) layout).getSelectedItemPosition() == 0) && submit) {
                flag = true;
                label.setTextColor(getResources().getColor(R.color.text_color_secondary));
                ((TextView) ((ViewGroup) layout).getChildAt(0)).setTextColor(getResources().getColor(R.color.text_color_secondary));
            } else {
                label.setTextColor(getResources().getColor(R.color.text_color_primary));
                ((TextView) ((ViewGroup) layout).getChildAt(0)).setTextColor(getResources().getColor(R.color.text_color_primary));
            }
        }
        return flag;
    }
    public void objectiveSubmit(android.view.View view) throws Exception {
        ArrayList<String> dataPoints = new ArrayList<>();
        objectiveTeam = ((TextView) findViewById(R.id.objectiveTeam)).getText().toString();
        objectiveMatch = ((TextView) findViewById(R.id.objectiveMatch)).getText().toString();
        objectiveInitials = ((TextView) findViewById(R.id.objectiveInitials)).getText().toString();
        objectiveSandstormHatch = ((TextView) findViewById(R.id.objectiveSandstormHatch)).getText().toString();
        objectiveSandstormCargo = ((TextView) findViewById(R.id.objectiveSandstormCargo)).getText().toString();
        objectiveNotes = ((TextView) findViewById(R.id.objectiveNotes)).getText().toString();
        objectiveDate = Long.toString((new Date()).getTime());
        boolean objectiveQuals = false;
        if ((objectiveMatch.toUpperCase().charAt(0) == 'Q') && (objectiveMatch.toUpperCase().charAt(1) != 'F')) {
            objectiveQuals = true;
        } else if ((!(objectiveMatch.toUpperCase().charAt(0) == 'S')) && (!(objectiveMatch.toUpperCase().charAt(0) == 'F'))) {
            objectiveMatch = "Q" + objectiveMatch;
            objectiveQuals = true;
        }
        objectiveMatch = objectiveMatch.toUpperCase();
        dataPoints.add(objectiveEvent);
        dataPoints.add(objectiveTeam);
        dataPoints.add(objectiveMatch);
        dataPoints.add(objectiveInitials);
        dataPoints.add(objectiveSandstormStart);
        dataPoints.add(objectiveSandstormPreload);
        dataPoints.add(objectiveSandstormHatch);
        dataPoints.add(objectiveSandstormCargo);
        for (int i = 8; i > 3; i--) {
            dataPoints.add(Integer.toString(objectiveCargo[i % 5]));
        }
        for (int i = 8; i > 3; i--) {
            dataPoints.add(Integer.toString(objectiveHatch[i % 5]));
        }
        dataPoints.add(objectiveEndgameClimb);
        dataPoints.add(objectiveEndgameClimbHelp);
        dataPoints.add(objectiveNotes);
        dataPoints.add(objectiveDate);
        boolean flag = submitValidator(findViewById(R.id.objectiveLayout), true);
        if (flag) {
            Toast.makeText(getApplicationContext(), "Please fill out all fields before submitting", Toast.LENGTH_SHORT).show();
        } else {
            StringBuilder sb = new StringBuilder();
            for (String i : dataPoints) {
                sb.append(i);
                sb.append("|");
            }
            sb.append("}");
            String str = sb.toString();
            objectiveGetHTML(str, true);
            try {
                File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "2019ScoutingLogs");
                if (!(path.exists())) {
                    path.mkdir();
                }
                File log = new File(path, "objectiveLog.txt");
                FileOutputStream out = new FileOutputStream(log, true);
                OutputStreamWriter writer = new OutputStreamWriter(out);
                writer.append(str);
                writer.flush();
                writer.close();
                Toast.makeText(getApplicationContext(), "Successfully saved", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            objectiveReset(findViewById(R.id.objectiveReset));
        }
    }
    public void pitSubmit(android.view.View view) throws Exception {
        ArrayList<String> dataPoints = new ArrayList<>();
        pitTeam = ((TextView) findViewById(R.id.pitTeam)).getText().toString();
        pitRole = ((TextView) findViewById(R.id.pitRole)).getText().toString();
        pitInitials = ((TextView) findViewById(R.id.pitInitials)).getText().toString();
        pitSandstormFrontHatch = ((CheckBox) findViewById(R.id.pitSandstormFrontHatch)).isChecked();
        pitRobotSpeed = ((TextView) findViewById(R.id.pitRobotSpeed)).getText().toString();
        pitRobotWeight = ((TextView) findViewById(R.id.pitRobotWeight)).getText().toString();
        pitRobotDrivetrain = ((TextView) findViewById(R.id.pitRobotDrivetrain)).getText().toString();
        pitRobotHatchMechanism = ((TextView) findViewById(R.id.pitRobotHatchMechanism)).getText().toString();
        pitRobotCargoMechanism = ((TextView) findViewById(R.id.pitRobotCargoMechanism)).getText().toString();
        pitClimbMechanism = ((TextView) findViewById(R.id.pitClimbMechanism)).getText().toString();
        pitClimbHelpMechanism = ((TextView) findViewById(R.id.pitClimbHelpMechanism)).getText().toString();
        pitDriveTeamExperience = ((TextView) findViewById(R.id.pitDriveTeamExperience)).getText().toString();
        pitNotes = ((TextView) findViewById(R.id.pitNotes)).getText().toString();
        pitDate = Long.toString((new Date()).getTime());
        dataPoints.add(pitEvent);
        dataPoints.add(pitTeam);
        dataPoints.add(pitRole);
        dataPoints.add(pitInitials);
        dataPoints.add(pitSandstormInformation);
        dataPoints.add(Boolean.toString(pitSandstormFrontHatch));
        dataPoints.add(pitRobotSpeed);
        dataPoints.add(pitRobotWeight);
        dataPoints.add(pitRobotVision);
        dataPoints.add(pitRobotDrivetrain);
        dataPoints.add(pitRobotHatchMechanism);
        dataPoints.add(pitRobotCargoMechanism);
        dataPoints.add(pitClimbMechanism);
        dataPoints.add(pitClimbHelpMechanism);
        dataPoints.add(pitDriveTeamExperience);
        dataPoints.add(pitNotes);
        dataPoints.add(pitDate);
        boolean flag = submitValidator(findViewById(R.id.pitLayout), true);
        if (flag) {
            Toast.makeText(getApplicationContext(), "Please fill out all fields before submitting", Toast.LENGTH_SHORT).show();
        } else {
            StringBuilder sb = new StringBuilder();
            for (String i : dataPoints) {
                sb.append(i);
                sb.append("|");
            }
            sb.append("}");
            String str = sb.toString();
            pitGetHTML(str, true);
            try {
                File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "2019ScoutingLogs");
                if (!(path.exists())) {
                    path.mkdir();
                }
                File log = new File(path, "pitLog.txt");
                FileOutputStream out = new FileOutputStream(log, true);
                OutputStreamWriter writer = new OutputStreamWriter(out);
                writer.append(str);
                writer.flush();
                writer.close();
                Toast.makeText(getApplicationContext(), "Successfully saved", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pitReset(findViewById(R.id.pitReset));
        }
    }
    public void sendLoadFile(android.view.View view) throws IOException {
        String type = sendFileSpinner.equals("Complete Log") ? "" : "Error";
        String str = sendFileSource.toLowerCase() + type + "Log.txt";
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "2019ScoutingLogs");
        if (!(path.exists())) {
            path.mkdir();
        }
        File log = new File(path, str);
        String rawText = "";
        BufferedReader BR = new BufferedReader(new FileReader(log));
        String temp;
        while ((temp = BR.readLine()) != null) {
            rawText += temp;
        }
        sendText = rawText;
        ((TextInputEditText) findViewById(R.id.sendText)).setText(sendText);
    }
    public void pairDevice(android.view.View v, int i) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            Object[] devices = pairedDevices.toArray();
            BluetoothDevice device = (BluetoothDevice) devices[i];
            AcceptThread accept = new AcceptThread();
            accept.start();
            ConnectThread connect = new ConnectThread(device, sendUUID);
            connect.start();
        }
    }
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        public ConnectThread(BluetoothDevice device, UUID uuid) {
            mmDevice = device;
            deviceUUID = uuid;
        }
        public void run() {
            BluetoothSocket tmp = null;
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(sendUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
            try {
                mmSocket.connect();
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException el) {
                    el.printStackTrace();
                }
            }
            connected(mmSocket);
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void connected(BluetoothSocket mmSocket) {
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    final String incomingMessage = new String(buffer, 0, bytes);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextInputEditText) findViewById(R.id.sendText)).setText(incomingMessage);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendMessage(View v) {
        byte[] bytes = ((TextInputEditText) findViewById(R.id.sendText)).getText().toString().getBytes();
        mConnectedThread.write(bytes);
    }
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        public AcceptThread(){
            BluetoothServerSocket tmp = null ;
            try {
                tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("appname", sendUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmServerSocket = tmp;
        }
        public void run(){
            BluetoothSocket socket = null;
            try {
                socket = mmServerSocket.accept();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(socket != null){
                connected(socket);
            }
        }
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendUpload(android.view.View view) {
        boolean isObjective = (view.getId() == R.id.sendObjective);
        String[] matchesList = ((TextInputEditText) findViewById(R.id.sendText)).getText().toString().split("\\}");
        ArrayList<String> matches = new ArrayList<String>(Arrays.asList(matchesList));
        while (matches.size() > 0) {
            String finalString = "";
            while ((finalString.length() < 1000) && (matches.size() > 0)) {
                finalString += matches.get(0) + "}";
                matches.remove(0);
            }
            try {
                if (isObjective) {
                    objectiveGetHTML(finalString, false);
                } else {
                    pitGetHTML(finalString, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void settingsSaveOID(android.view.View view) throws IOException {
        String OID = ((EditText) findViewById(R.id.settingsOID)).getText().toString();
        File path = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "2019ScoutingLogs"), "settingsLogs");
        if (!(path.exists())) {
            path.mkdir();
        }
        File log = new File(path, "OID.txt");
        FileOutputStream out = new FileOutputStream(log, false);
        OutputStreamWriter writer = new OutputStreamWriter(out);
        writer.write(OID);
        writer.flush();
        writer.close();
    }

    public void settingsSavePID(android.view.View view) throws IOException {
        String PID = ((EditText) findViewById(R.id.settingsPID)).getText().toString();
        File path = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "2019ScoutingLogs"), "settingsLogs");
        if (!(path.exists())) {
            path.mkdir();
        }
        File log = new File(path, "PID.txt");
        FileOutputStream out = new FileOutputStream(log, false);
        OutputStreamWriter writer = new OutputStreamWriter(out);
        writer.write(PID);
        writer.flush();
        writer.close();
    }
}         /*===========\
         /...............\
        /..................\
       /....................\
      /.....................\
     /......................\\\_____
    /.......................\\\\\\\\\________
   |....................................\\\\\\______
   |............................................======\____
    \...................................................===\___
     \........................................................\_
       \........................................................\
         \.......................................................\
            \.................................._====_._............\
               \........................... ../            *-.._......\
                 \..........................|                   \_____/*
                   \........................|                     \----\
                     \......................|   FROM 1257          |....\
                       \=\..................|        WITH LOVE     |.....\
                        \==\.................\                    /......|
                         \==\\.................\                 /......|
                          \=\.\\.................______________/......./
                            \=..\.................................../
                             \=\..\................................/
                               \=...\............................./
                                 \=\..\\\________________________.
                                  \..\\__.............\.
                                   \..\\..---__..........\.
                                    \..\\.......\............\.
                                      \..\\.......\\..............\.
                                        \..\\.......\  \\._...........\.
                                         \...\\.......\     --\._........\
                                          \...| \.......\         \_._.....\
                                           \..|   \.......\            \....|
                                            \/       \......\             \/
                                                        \.....\
                                                          \.../
                                                            */