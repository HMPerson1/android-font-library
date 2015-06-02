package nullnull.fonttest;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nullnull.fontslibrary.TypefaceRecord;
import nullnull.fontslibrary.TypefaceUtils;
import nullnull.fontslibrary.TypefacesAdapter;


public class MainActivity extends ListActivity {

    private List<TypefaceRecord> fontList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Set<TypefaceRecord> fonts;
        try {
            fonts = TypefaceUtils.typefaces();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            e.printStackTrace();
            Toast.makeText(this, "Error: Could not get font list.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        fontList = new ArrayList<>(fonts);

        setListAdapter(new TypefacesAdapter(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                fontList));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TypefaceRecord font = fontList.get(position);
        Set<String> otherNamesSet = font.getOtherNames();

        if (otherNamesSet.size() > 0) {
            SpannableString otherNames = new SpannableString(otherNamesSet.toString());
            otherNames.setSpan(new TypefaceSpan(font.getName()), 0, otherNames.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Toast.makeText(this, new SpannableStringBuilder("Other Names: ").append(otherNames), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No other names", Toast.LENGTH_LONG).show();
        }
    }
}
