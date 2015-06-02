package nullnull.fontslibrary;

import android.graphics.Typeface;

import java.util.Collections;
import java.util.Formatter;
import java.util.Set;

/**
 * Stores information about a {@link Typeface}.
 */
public final class TypefaceRecord {
    /**
     * The TypefaceRecord for {@link Typeface#DEFAULT}. This object's
     * {@link TypefaceRecord#getOtherNames()} may not be complete.
     */
    public static final TypefaceRecord DEFAULT = new TypefaceRecord("DEFAULT", Typeface.DEFAULT, Collections.EMPTY_SET);
    private final String name;
    private final Typeface typeface;
    private final Set<String> otherNames;

    public TypefaceRecord(String name, Typeface typeface, Set<String> otherNames) {
        this.name = name;
        this.typeface = typeface;
        this.otherNames = otherNames;
    }

    @Override
    public String toString() {
        return new Formatter().format(
                "TypefaceRecord{name=%s,typeface=%s,otherNames=%s}",
                name, typeface, otherNames)
                .toString();
    }

    public String getName() {
        return name;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public Set<String> getOtherNames() {
        return otherNames;
    }
}
