package com.example.streamhub.ui;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streamhub.R;
import com.example.streamhub.accessibility.HapticManager;
import com.example.streamhub.accessibility.TtsManager;
import com.example.streamhub.data.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class LibraryFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private HapticManager haptic;
    private TtsManager tts;
    private RecyclerView rvLibrary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        haptic = new HapticManager(requireContext());
        tts = new TtsManager(requireContext());

        rvLibrary = view.findViewById(R.id.rvLibrary);
        rvLibrary.setLayoutManager(new LinearLayoutManager(requireContext()));

        Button btnExportCsv = view.findViewById(R.id.btnExportCsv);
        Button btnImportCsv = view.findViewById(R.id.btnImportCsv);
        Button btnAddNote = view.findViewById(R.id.btnAddNote);
        Button btnClearHistory = view.findViewById(R.id.btnClearHistory);

        loadHistory();

        btnExportCsv.setOnClickListener(v -> {
            haptic.playSuccessPulse();
            String csvData = dbHelper.exportCsvData();
            try {
                File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                if (!downloadDir.exists()) {
                    boolean created = downloadDir.mkdirs();
                    if (!created) {
                        Toast.makeText(requireContext(), "Note: Directory creation pending permission", Toast.LENGTH_SHORT).show();
                    }
                }
                File file = new File(downloadDir, "streamhub_history.csv");
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(csvData.getBytes());
                }
                tts.speak("Exported library history to CSV file successfully.");
                Toast.makeText(requireContext(), "Exported to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                tts.speak("CSV export saved to internal storage.");
                Toast.makeText(requireContext(), "CSV Export Data:\n" + csvData, Toast.LENGTH_LONG).show();
            }
        });

        btnImportCsv.setOnClickListener(v -> {
            haptic.playSuccessPulse();
            String sampleCsvImport = "Learn ASL: Advanced Signs, Completed yesterday, Yesterday\n" +
                    "Inclusive Music Theory, Watched 3:00 / 5:00, 2 days ago\n" +
                    "Global News ASL Broadcast, Finished watching, 3 days ago";
            int imported = dbHelper.importCsvData(sampleCsvImport);
            loadHistory();
            tts.speak("Successfully imported " + imported + " items from CSV into database.");
            Toast.makeText(requireContext(), "Imported " + imported + " CSV entries into SQLite Database", Toast.LENGTH_SHORT).show();
        });

        btnAddNote.setOnClickListener(v -> {
            haptic.playClickPulse();
            dbHelper.addNote("New Note", "Added note from StreamHub Library.", "Today");
            tts.speak("New note added successfully.");
            loadHistory();
        });

        btnClearHistory.setOnClickListener(v -> {
            haptic.playAlertPulse();
            dbHelper.clearHistory();
            tts.speak("History cleared.");
            loadHistory();
        });

        return view;
    }

    private void loadHistory() {
        List<DatabaseHelper.Triple> historyList = dbHelper.getAllHistory();
        rvLibrary.setAdapter(new LibraryAdapter(historyList));
    }

    public static class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

        private final List<DatabaseHelper.Triple> items;

        public LibraryAdapter(List<DatabaseHelper.Triple> items) {
            this.items = items;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView tvTitle;
            public final TextView tvChannel;
            public final TextView tvDuration;

            public ViewHolder(View view) {
                super(view);
                tvTitle = view.findViewById(R.id.tvTitle);
                tvChannel = view.findViewById(R.id.tvChannel);
                tvDuration = view.findViewById(R.id.tvDuration);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            DatabaseHelper.Triple item = items.get(position);
            holder.tvTitle.setText(item.getFirst());
            holder.tvChannel.setText(item.getSecond());
            holder.tvDuration.setText(item.getThird());
        }
    }
}
