package pl.sevanntek.plantreningowy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final int YEAR = 2026;
    private static final int RED = Color.rgb(211, 47, 47);
    private static final int GREEN = Color.rgb(46, 125, 50);
    private static final int AMBER = Color.rgb(245, 124, 0);
    private static final int TEXT = Color.rgb(32, 33, 36);
    private static final int MUTED = Color.rgb(95, 99, 104);
    private static final int BORDER = Color.rgb(220, 223, 226);
    private static final int PANEL = Color.WHITE;

    private final Locale pl = new Locale("pl", "PL");
    private SharedPreferences prefs;
    private int currentMonth = Calendar.OCTOBER;
    private int selectedDay = 1;

    private LinearLayout root;
    private TextView monthTitle;
    private TextView monthPercent;
    private GridLayout calendarGrid;
    private TextView selectedDate;
    private TextView selectedSub;
    private TextView dayPercent;
    private View dayDot;
    private TextView trainingParts;
    private CheckBox trainingDone;
    private View trainingStatusDot;
    private RadioGroup cardioGroup;
    private CheckBox cardioDone;
    private View cardioStatusDot;
    private LinearLayout goalsBox;
    private final List<GoalRow> goalRows = new ArrayList<>();

    private final String[][] goals = {
            {"diet", "Dieta zgodna z planem"},
            {"calories", "Kalorie w założonym zakresie"},
            {"protein", "Białko około 200 g"},
            {"nosweets", "Brak słodyczy"},
            {"water", "Woda / nawodnienie"},
            {"creatine", "Kreatyna"},
            {"supplements", "Pozostałe suplementy"},
            {"sleep", "Sen i regeneracja"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("fit_plan_2026", MODE_PRIVATE);
        setTitle("Plan treningowy");
        buildUi();
        showMonth(Calendar.OCTOBER);
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(14), dp(16), dp(14), dp(24));
        root.setBackgroundColor(Color.rgb(247, 248, 249));
        scroll.addView(root, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        monthTitle = text("Październik 2026", 24, Typeface.BOLD, TEXT);
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        top.addView(monthTitle, titleLp);
        LinearLayout pctBox = new LinearLayout(this);
        pctBox.setOrientation(LinearLayout.VERTICAL);
        pctBox.setGravity(Gravity.END);
        pctBox.addView(text("Realizacja miesiąca", 12, Typeface.NORMAL, MUTED));
        monthPercent = text("0%", 26, Typeface.BOLD, TEXT);
        pctBox.addView(monthPercent);
        top.addView(pctBox);
        root.addView(top);

        LinearLayout tabs = new LinearLayout(this);
        tabs.setOrientation(LinearLayout.HORIZONTAL);
        tabs.setPadding(0, dp(12), 0, dp(12));
        addMonthButton(tabs, "Paź", Calendar.OCTOBER);
        addMonthButton(tabs, "Lis", Calendar.NOVEMBER);
        addMonthButton(tabs, "Gru", Calendar.DECEMBER);
        root.addView(tabs);

        calendarGrid = new GridLayout(this);
        calendarGrid.setColumnCount(4);
        calendarGrid.setUseDefaultMargins(false);
        LinearLayout calPanel = panel();
        calPanel.addView(calendarGrid, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(calPanel);

        LinearLayout dayHeader = new LinearLayout(this);
        dayHeader.setOrientation(LinearLayout.HORIZONTAL);
        dayHeader.setGravity(Gravity.CENTER_VERTICAL);
        dayHeader.setPadding(0, dp(16), 0, dp(8));
        LinearLayout dateBox = new LinearLayout(this);
        dateBox.setOrientation(LinearLayout.VERTICAL);
        selectedDate = text("", 22, Typeface.BOLD, TEXT);
        selectedSub = text("", 13, Typeface.NORMAL, MUTED);
        dateBox.addView(selectedDate);
        dateBox.addView(selectedSub);
        dayHeader.addView(dateBox, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        FrameLayout badge = new FrameLayout(this);
        dayDot = new View(this);
        dayDot.setBackground(makeCircle(RED));
        FrameLayout.LayoutParams dotLp = new FrameLayout.LayoutParams(dp(58), dp(58));
        dotLp.gravity = Gravity.CENTER;
        badge.addView(dayDot, dotLp);
        dayPercent = text("0%", 16, Typeface.BOLD, Color.WHITE);
        dayPercent.setGravity(Gravity.CENTER);
        badge.addView(dayPercent, new FrameLayout.LayoutParams(dp(58), dp(58), Gravity.CENTER));
        dayHeader.addView(badge, new LinearLayout.LayoutParams(dp(64), dp(64)));
        root.addView(dayHeader);

        LinearLayout trainingPanel = panel();
        trainingPanel.addView(sectionTitle("Trening siłowy"));
        trainingParts = text("", 17, Typeface.BOLD, TEXT);
        trainingParts.setPadding(0, dp(4), 0, dp(6));
        trainingPanel.addView(trainingParts);
        LinearLayout trainingCheckRow = new LinearLayout(this);
        trainingCheckRow.setOrientation(LinearLayout.HORIZONTAL);
        trainingCheckRow.setGravity(Gravity.CENTER_VERTICAL);
        trainingStatusDot = new View(this);
        trainingCheckRow.addView(trainingStatusDot, new LinearLayout.LayoutParams(dp(16), dp(16)));
        trainingDone = check("Wykonany");
        LinearLayout.LayoutParams trainingCheckLp = new LinearLayout.LayoutParams(0, dp(50), 1f);
        trainingCheckLp.setMargins(dp(8), 0, 0, 0);
        trainingCheckRow.addView(trainingDone, trainingCheckLp);
        trainingDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                putBool(key("training"), isChecked);
                refresh();
            }
        });
        trainingPanel.addView(trainingCheckRow);
        root.addView(trainingPanel);

        LinearLayout cardioPanel = panel();
        cardioPanel.addView(sectionTitle("Cardio"));
        cardioGroup = new RadioGroup(this);
        cardioGroup.setOrientation(RadioGroup.VERTICAL);
        cardioPanel.addView(cardioGroup);
        LinearLayout cardioCheckRow = new LinearLayout(this);
        cardioCheckRow.setOrientation(LinearLayout.HORIZONTAL);
        cardioCheckRow.setGravity(Gravity.CENTER_VERTICAL);
        cardioStatusDot = new View(this);
        cardioCheckRow.addView(cardioStatusDot, new LinearLayout.LayoutParams(dp(16), dp(16)));
        cardioDone = check("Cardio wykonane");
        LinearLayout.LayoutParams cardioCheckLp = new LinearLayout.LayoutParams(0, dp(50), 1f);
        cardioCheckLp.setMargins(dp(8), 0, 0, 0);
        cardioCheckRow.addView(cardioDone, cardioCheckLp);
        cardioDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                putBool(key("cardio"), isChecked);
                refresh();
            }
        });
        cardioPanel.addView(cardioCheckRow);
        root.addView(cardioPanel);

        LinearLayout goalsPanel = panel();
        goalsPanel.addView(sectionTitle("Cele dnia"));
        goalsBox = new LinearLayout(this);
        goalsBox.setOrientation(LinearLayout.VERTICAL);
        goalsPanel.addView(goalsBox);
        for (String[] g : goals) {
            GoalRow row = new GoalRow(g[0], g[1]);
            goalRows.add(row);
            goalsBox.addView(row.container);
        }
        root.addView(goalsPanel);

        setContentView(scroll);
    }

    private void addMonthButton(LinearLayout parent, String label, int month) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(16);
        b.setAllCaps(false);
        b.setOnClickListener(v -> showMonth(month));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(48), 1f);
        lp.setMargins(dp(3), 0, dp(3), 0);
        parent.addView(b, lp);
    }

    private void showMonth(int month) {
        currentMonth = month;
        selectedDay = Math.min(selectedDay, daysInMonth(month));
        monthTitle.setText(monthName(month) + " 2026");
        renderCalendar();
        refresh();
    }

    private void renderCalendar() {
        calendarGrid.removeAllViews();
        int days = daysInMonth(currentMonth);
        for (int day = 1; day <= days; day++) {
            final int d = day;
            Calendar c = Calendar.getInstance(pl);
            c.clear();
            c.set(YEAR, currentMonth, day);
            int dow = c.get(Calendar.DAY_OF_WEEK);
            boolean weekend = dow == Calendar.SATURDAY || dow == Calendar.SUNDAY;
            int pct = score(day);

            LinearLayout cell = new LinearLayout(this);
            cell.setOrientation(LinearLayout.VERTICAL);
            cell.setPadding(dp(8), dp(7), dp(8), dp(7));
            cell.setGravity(Gravity.CENTER_HORIZONTAL);
            cell.setBackground(makeRounded(day == selectedDay ? Color.rgb(232, 238, 242) : Color.WHITE, BORDER));

            TextView date = text(shortDay(dow) + " • " + day + " " + monthShort(currentMonth), 13, weekend ? Typeface.BOLD : Typeface.NORMAL, TEXT);
            date.setGravity(Gravity.CENTER);
            cell.addView(date, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            LinearLayout small = new LinearLayout(this);
            small.setGravity(Gravity.CENTER);
            TextView p = text(pct + "%", 11, Typeface.BOLD, MUTED);
            small.addView(p);
            View dot = new View(this);
            dot.setBackground(makeCircle(pct == 100 ? GREEN : pct > 0 ? AMBER : RED));
            LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(dp(10), dp(10));
            dlp.setMargins(dp(6), 0, 0, 0);
            small.addView(dot, dlp);
            cell.addView(small);
            cell.setOnClickListener(v -> {
                selectedDay = d;
                renderCalendar();
                refresh();
            });

            GridLayout.LayoutParams glp = new GridLayout.LayoutParams();
            glp.width = 0;
            glp.height = dp(62);
            glp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            glp.setMargins(dp(3), dp(3), dp(3), dp(3));
            calendarGrid.addView(cell, glp);
        }
    }

    private void refresh() {
        Calendar c = Calendar.getInstance(pl);
        c.clear();
        c.set(YEAR, currentMonth, selectedDay);
        int dow = c.get(Calendar.DAY_OF_WEEK);
        boolean weekend = dow == Calendar.SATURDAY || dow == Calendar.SUNDAY;
        String title = longDay(dow) + ", " + selectedDay + " " + monthShort(currentMonth);
        selectedDate.setText(title);
        selectedDate.setTypeface(Typeface.DEFAULT, weekend ? Typeface.BOLD : Typeface.BOLD);

        Plan plan = planForDay(selectedDay);
        selectedSub.setText(plan.rest ? "Dzień regeneracyjny" : "Plan treningowy");
        trainingParts.setText(plan.rest ? "Wolny" : plan.name + (plan.abs ? " + brzuch" : ""));
        trainingDone.setEnabled(!plan.rest);
        trainingDone.setChecked(plan.rest || prefs.getBoolean(key("training"), false));
        trainingDone.setText(plan.rest ? "Wolny" : (trainingDone.isChecked() ? "Wykonany" : "Do wykonania"));
        trainingStatusDot.setBackground(makeCircle(plan.rest || trainingDone.isChecked() ? GREEN : RED));

        cardioGroup.setOnCheckedChangeListener(null);
        cardioGroup.removeAllViews();
        int savedChoice = prefs.getInt(key("cardioChoice"), 0);
        for (int i = 0; i < 2; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(plan.cardio[i]);
            rb.setTextSize(15);
            rb.setTextColor(TEXT);
            rb.setId(100 + i);
            rb.setChecked(i == savedChoice);
            cardioGroup.addView(rb, new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(50)));
        }
        cardioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int choice = checkedId - 100;
            if (choice >= 0 && choice < 2) {
                prefs.edit().putInt(key("cardioChoice"), choice).apply();
            }
        });
        cardioDone.setChecked(prefs.getBoolean(key("cardio"), false));
        cardioStatusDot.setBackground(makeCircle(cardioDone.isChecked() ? GREEN : RED));

        for (GoalRow row : goalRows) row.update();

        int pct = score(selectedDay);
        dayPercent.setText(pct + "%");
        dayDot.setBackground(makeCircle(pct == 100 ? GREEN : pct > 0 ? AMBER : RED));
        monthPercent.setText(monthScore() + "%");
    }

    private int score(int day) {
        Plan p = planForDay(day);
        int done = 0;
        int total = 0;
        if (!p.rest) {
            total++;
            if (prefs.getBoolean(keyFor(day, "training"), false)) done++;
        }
        total++;
        if (prefs.getBoolean(keyFor(day, "cardio"), false)) done++;
        for (String[] g : goals) {
            total++;
            if (prefs.getBoolean(keyFor(day, g[0]), false)) done++;
        }
        return total == 0 ? 0 : Math.round(done * 100f / total);
    }

    private int monthScore() {
        int sum = 0;
        int days = daysInMonth(currentMonth);
        for (int d = 1; d <= days; d++) sum += score(d);
        return Math.round(sum / (float) days);
    }

    private Plan planForDay(int day) {
        if (day == 8 || day == 17 || day == 25) {
            return new Plan("Wolny", true, false,
                    "Bieżnia / spacer – spokojnie 45 min",
                    "Rower stacjonarny – lekko 45 min");
        }
        int trainingIndex = 0;
        for (int d = 1; d < day; d++) {
            if (d != 8 && d != 17 && d != 25) trainingIndex++;
        }
        int type = trainingIndex % 3;
        boolean abs = trainingIndex % 2 == 0;
        if (type == 0) return new Plan("Klatka + barki + triceps", false, abs,
                "Bieżnia – szybki marsz 30 min", "Rower stacjonarny – tempo umiarkowane 30 min");
        if (type == 1) return new Plan("Plecy + biceps + przedramiona", false, abs,
                "Bieżnia – szybki marsz 30 min", "Rower stacjonarny – tempo umiarkowane 30 min");
        return new Plan("Nogi + łydki + biceps + przedramiona", false, abs,
                "Bieżnia – spokojny marsz 15 min", "Rower stacjonarny – lekko 15 min");
    }

    private String key(String field) { return keyFor(selectedDay, field); }
    private String keyFor(int day, String field) { return YEAR + "-" + (currentMonth + 1) + "-" + day + "-" + field; }
    private void putBool(String k, boolean v) { prefs.edit().putBoolean(k, v).apply(); }

    private int daysInMonth(int month) {
        Calendar c = Calendar.getInstance(pl);
        c.clear();
        c.set(YEAR, month, 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private String monthName(int month) {
        String[] names = {"Styczeń","Luty","Marzec","Kwiecień","Maj","Czerwiec","Lipiec","Sierpień","Wrzesień","Październik","Listopad","Grudzień"};
        return names[month];
    }
    private String monthShort(int month) {
        String[] names = {"Sty","Lut","Mar","Kwi","Maj","Cze","Lip","Sie","Wrz","Paź","Lis","Gru"};
        return names[month];
    }
    private String shortDay(int dow) {
        String[] n = {"","Nie","Pon","Wt","Śr","Czw","Pt","Sob"};
        return n[dow];
    }
    private String longDay(int dow) {
        String[] n = {"","Niedziela","Poniedziałek","Wtorek","Środa","Czwartek","Piątek","Sobota"};
        return n[dow];
    }

    private LinearLayout panel() {
        LinearLayout p = new LinearLayout(this);
        p.setOrientation(LinearLayout.VERTICAL);
        p.setPadding(dp(14), dp(12), dp(14), dp(12));
        p.setBackground(makeRounded(PANEL, BORDER));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, dp(6), 0, dp(6));
        p.setLayoutParams(lp);
        return p;
    }

    private TextView sectionTitle(String s) { return text(s, 18, Typeface.BOLD, TEXT); }
    private CheckBox check(String s) {
        CheckBox cb = new CheckBox(this);
        cb.setText(s);
        cb.setTextSize(16);
        cb.setTextColor(TEXT);
        cb.setMinHeight(dp(48));
        return cb;
    }
    private TextView text(String s, int sp, int style, int color) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(sp);
        t.setTextColor(color);
        t.setTypeface(Typeface.DEFAULT, style);
        return t;
    }

    private android.graphics.drawable.GradientDrawable makeRounded(int color, int stroke) {
        android.graphics.drawable.GradientDrawable g = new android.graphics.drawable.GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(12));
        g.setStroke(dp(1), stroke);
        return g;
    }
    private android.graphics.drawable.GradientDrawable makeCircle(int color) {
        android.graphics.drawable.GradientDrawable g = new android.graphics.drawable.GradientDrawable();
        g.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        g.setColor(color);
        return g;
    }
    private int dp(int value) { return Math.round(value * getResources().getDisplayMetrics().density); }

    private class GoalRow {
        final String key;
        final String label;
        final LinearLayout container;
        final View dot;
        final CheckBox check;

        GoalRow(String key, String label) {
            this.key = key;
            this.label = label;
            container = new LinearLayout(MainActivity.this);
            container.setOrientation(LinearLayout.HORIZONTAL);
            container.setGravity(Gravity.CENTER_VERTICAL);
            container.setPadding(0, dp(3), 0, dp(3));
            dot = new View(MainActivity.this);
            container.addView(dot, new LinearLayout.LayoutParams(dp(16), dp(16)));
            check = check(label);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(50), 1f);
            lp.setMargins(dp(8), 0, 0, 0);
            container.addView(check, lp);
            check.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (buttonView.isPressed()) {
                    putBool(key(key), isChecked);
                    renderCalendar();
                    refresh();
                }
            });
        }
        void update() {
            boolean v = prefs.getBoolean(key(key), false);
            check.setChecked(v);
            dot.setBackground(makeCircle(v ? GREEN : RED));
        }
    }

    private static class Plan {
        final String name;
        final boolean rest;
        final boolean abs;
        final String[] cardio;
        Plan(String name, boolean rest, boolean abs, String c1, String c2) {
            this.name = name; this.rest = rest; this.abs = abs; this.cardio = new String[]{c1, c2};
        }
    }
}
