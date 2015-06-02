package nullnull.fontslibrary;

import android.graphics.Typeface;

import java.util.Formatter;
import java.util.Set;

/**
 * Stores information about a {@link Typeface}.
 */
public final class TypefaceRecord {
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
