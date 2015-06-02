package nullnull.fontslibrary;

import android.graphics.Typeface;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Utilities for getting {@link Typeface}s.
 * <br><br>
 * Actually it's just one method, so I guess it should just be:
 * <br>
 * 'A utility for getting {@link Typeface}s'.
 */
public class TypefaceUtils {
    /**
     * A Comparator that compares the names of two TypefaceRecords
     */
    private static final Comparator<TypefaceRecord> TYPEFACE_RECORD_COMPARATOR = new Comparator<TypefaceRecord>() {
        @Override
        public int compare(TypefaceRecord lhs, TypefaceRecord rhs) {
            return lhs.getName().compareToIgnoreCase(rhs.getName());
        }
    };

    private static Set<TypefaceRecord> cache;

    /**
     * Utility class -- private constructor
     */
    private TypefaceUtils() {
    }

    /**
     * Gets a Set of typefaces installed on this system.
     *
     * @return A Set of TypefaceRecords
     * @throws IllegalAccessException if reflection magic fails
     * @throws NoSuchFieldException   if reflection magic fails
     */
    public static Set<TypefaceRecord> typefaces() throws IllegalAccessException, NoSuchFieldException {
        if (cache == null) {
            cache = sortRecords(mkRecords(dedupTypefaces(getRawTypefaceList())));
        }
        return cache;
    }

    /**
     * Try to impose some kind of order on the records.
     * Currently, this groups by (sans-)serif and monospace.
     *
     * @param records a set of records to sort
     * @return (somewhat) sorted records
     */
    private static Set<TypefaceRecord> sortRecords(Set<TypefaceRecord> records) {
        //@formatter:off
        Set<TypefaceRecord> sansSerifs = new TreeSet<>(TYPEFACE_RECORD_COMPARATOR);
        Set<TypefaceRecord> serifs = new TreeSet<>(TYPEFACE_RECORD_COMPARATOR);
        Set<TypefaceRecord> sansSerifMonos = new TreeSet<>(TYPEFACE_RECORD_COMPARATOR);
        Set<TypefaceRecord> serifMonos = new TreeSet<>(TYPEFACE_RECORD_COMPARATOR);
        Set<TypefaceRecord> others = new TreeSet<>(TYPEFACE_RECORD_COMPARATOR);
        //@formatter:on

        for (TypefaceRecord r : records) {
            String name = r.getName();
            if (name.startsWith("sans-serif-monospace")) {
                sansSerifMonos.add(r);
            } else if (name.startsWith("serif-monospace")) {
                serifMonos.add(r);
            } else if (name.startsWith("sans-serif")) {
                sansSerifs.add(r);
            } else if (name.startsWith("serif")) {
                serifs.add(r);
            } else {
                others.add(r);
            }
        }

        Set<TypefaceRecord> ret = new LinkedHashSet<>(others);
        ret.addAll(sansSerifs);
        ret.addAll(serifs);
        ret.addAll(sansSerifMonos);
        ret.addAll(serifMonos);

        return ret;
    }

    /**
     * Convert Typeface->[String] associations to TypefaceRecords.
     *
     * @param ret A Map from Typefaces to its names
     * @return a Set of TypefaceRecords
     */
    private static Set<TypefaceRecord> mkRecords(Map<Typeface, ? extends Set<String>> ret) {
        Set<TypefaceRecord> records = new HashSet<>();
        for (Map.Entry<Typeface, ? extends Set<String>> e : ret.entrySet()) {
            Set<String> names = e.getValue();
            if (names.size() == 0) throw new IllegalArgumentException("Typeface with no names");

            // Try to find the most generic name (the one that begins with serif or sans-serif)
            // using the first name as a default
            String gName = names.iterator().next();
            for (String name : names) {
                if (name.startsWith("serif") || name.startsWith("sans-serif")) {
                    gName = name;
                }
            }

            names.remove(gName);
            records.add(new TypefaceRecord(gName, e.getKey(), names));
        }
        return records;
    }

    /**
     * Removes duplicate Typefaces that are under different names and makes a list of all the names
     * for a Typeface
     *
     * @param typefaceMap A Map from names to Typefaces
     * @return A Map from Typefaces to its (many) names
     */
    private static Map<Typeface, SortedSet<String>> dedupTypefaces(Map<String, Typeface> typefaceMap) {
        Map<Typeface, SortedSet<String>> ret = new HashMap<>();
        for (Map.Entry<String, Typeface> e : typefaceMap.entrySet()) {
            Typeface typeface = e.getValue();
            String name = e.getKey();
            if (ret.containsKey(typeface)) {
                ret.get(typeface).add(name);
            } else {
                SortedSet<String> s = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
                s.add(name);
                ret.put(typeface, s);
            }
        }
        return ret;
    }

    /**
     * Returns the list of installed Typefaces
     *
     * @return the list of Typefaces and their names
     * @throws NoSuchFieldException   if reflection magic fails
     * @throws IllegalAccessException if reflection magic fails
     */
    private static Map<String, Typeface> getRawTypefaceList() throws NoSuchFieldException, IllegalAccessException {
        Field fmField = Typeface.class.getDeclaredField("sSystemFontMap");
        fmField.setAccessible(true);
        //noinspection unchecked
        return (Map<String, Typeface>) fmField.get(null);
    }
}
