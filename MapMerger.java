package JavaGenericsMatrix;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.Map.Entry;

public final class MapMerger{

    private static record MergeParameters<K,V>(K index, V x, V y ){
        public MergeParameters<K,V> setX(Map.Entry<K,V> contents){
            return new MergeParameters<K,V>(contents.getKey(), contents.getValue(), y);
        }
        public MergeParameters<K,V> setY(Map.Entry<K,V> contents){
            return new MergeParameters<K,V>(contents.getKey(), x, contents.getValue());
        }
    }

    public static <K, V> NavigableMap<K, V> merge(
            PeekingIterator<Entry<K, V>> itThis,
            PeekingIterator<Entry<K, V>> itOther,
            Comparator<? super K> comparator,
            BinaryOperator<V> op,
            K origin,
            V zero) {

        NavigableMap<K, V> merged = new TreeMap<>();
        //get rid of zeros later
        while (itThis.hasNext() || itOther.hasNext()) {
            MergeParameters<K, V> mergeParameters = new MergeParameters<>(origin, zero, zero);
            mergeParameters = chooseStep(itThis, itOther, comparator, mergeParameters, zero);
            V value = op.apply(mergeParameters.x(), mergeParameters.y());
            if (!value.equals(zero)) {
                merged.put(mergeParameters.index(), value);
            }
            iterate(itThis, itOther);
        }
        return merged;
    }

    private static <K,V> MergeParameters<K,V> stepParameters(
        PeekingIterator<Entry<K, V>> itThis,
        PeekingIterator<Entry<K, V>> itOther,
        Comparator<? super K> comparator,
        MergeParameters<K, V> mergeParameters){

            Function<Entry<K, V>, MergeParameters<K, V>> parameters = entry -> mergeParameters.setX(entry);

            Entry<K,V> entryThis = getEntry(itThis);
            Entry<K,V> entryOther = getEntry(itOther);

            int comparison = comparator.compare(entryThis.getKey(), entryOther.getKey());
            if(comparison == 0){
                return mergeParameters.setX(entryThis).setY(entryOther);
            }
            else if (comparison < 0){
                return stepParameters(itThis, parameters);
            }
            else{
                return stepParameters(itOther, parameters);
            }
            
    }

    private static <K,V> MergeParameters<K,V> stepParameters(
        PeekingIterator <Entry<K, V>> iterator,
        Function<Entry<K, V>, MergeParameters<K, V>> parameters){
            return parameters.apply(iterator.peek().get());
        }
    
    public static <K,V> void iterate(PeekingIterator<Entry<K,V>> itThis, PeekingIterator<Entry<K,V>> itOther){
        if (itThis.hasNext()){
            itThis.next();
        }
        if (itOther.hasNext()){
            itOther.next();
        }
    }

    //helper method to determine which method to use
    private static <K,V> MergeParameters<K,V> chooseStep(
        PeekingIterator<Entry<K, V>> itThis,
        PeekingIterator<Entry<K, V>> itOther,
        Comparator<? super K> comparator,
        MergeParameters<K, V> mergeParameters,
        V zero) {
            Function<Entry<K, V>, MergeParameters<K, V>> parameters = entry -> mergeParameters.setX(entry);
            if(!itOther.hasNext()) {
                return stepParameters(itThis, parameters);
            } 
            else if (!itThis.hasNext()){
                return stepParameters(itOther, parameters);
            }
            return stepParameters(itThis, itOther, comparator, mergeParameters);
    }



    //helper method to get entry from iterator
    public static <K,V> Entry<K,V> getEntry(PeekingIterator<Entry<K,V>> iterator){
        return iterator.peek().get();
    }
}
