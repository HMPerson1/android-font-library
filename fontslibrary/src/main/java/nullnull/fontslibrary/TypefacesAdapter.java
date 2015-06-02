package nullnull.fontslibrary;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * A {@link android.widget.ListAdapter} for displaying {@link TypefaceRecord}s.
 */
public class TypefacesAdapter extends ArrayAdapter<TypefaceRecord> {
    private int mFieldId = 0;

    public TypefacesAdapter(Context context, int resource) {
        super(context, resource);
    }

    public TypefacesAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        mFieldId = textViewResourceId;
    }

    public TypefacesAdapter(Context context, int resource, TypefaceRecord[] objects) {
        super(context, resource, objects);
    }

    public TypefacesAdapter(Context context, int resource, int textViewResourceId, TypefaceRecord[] objects) {
        super(context, resource, textViewResourceId, objects);
        mFieldId = textViewResourceId;
    }

    public TypefacesAdapter(Context context, int resource, List<TypefaceRecord> objects) {
        super(context, resource, objects);
    }

    public TypefacesAdapter(Context context, int resource, int textViewResourceId, List<TypefaceRecord> objects) {
        super(context, resource, textViewResourceId, objects);
        mFieldId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return modifyView(position, super.getView(position, convertView, parent));
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return modifyView(position, super.getDropDownView(position, convertView, parent));
    }

    /**
     * Modifies a {@link View} to have the proper font.
     *
     * @param position the position of the view
     * @param view the view to modify
     * @return the modified view
     */
    private View modifyView(int position, View view) {
        TextView text = getTextView(view);
        TypefaceRecord item = getItem(position);

        text.setTypeface(item.getTypeface());
        text.setText(item.getName());

        return view;
    }

    /**
     * Finds the {@link TextView} from the given {@link View}.
     *
     * @param view the View to look in
     * @return the TextView
     */
    private TextView getTextView(View view) {
        // Copied from ArrayAdapter#createViewFromResource
        TextView text;
        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }
        return text;
    }
}
