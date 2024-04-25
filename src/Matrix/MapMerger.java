package Matrix;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.Map.Entry;


/**
 * Class that contains operations to merge matrices
 */
final class MapMerger{

    private static record MergeParameters<K,V>(K index, V x, V y ){
        public MergeParameters<K,V> setX(Map.Entry<K,V> contents){
            return new MergeParameters<K,V>(contents.getKey(), contents.getValue(), y);
        }
        public MergeParameters<K,V> setY(Map.Entry<K,V> contents){
            return new MergeParameters<K,V>(contents.getKey(), x, contents.getValue());
        }
    }

    
    /** 
     * Returns a navigable map of two peeking iterators merged at each individual index by the binary operation. Empty values are treated as zero.
     * 
     * @param itThis
     * @param itOther
     * @param comparator
     * @param op
     * @param origin
     * @param zero
     * @return NavigableMap<K, V>
     */
    public static <K, V> NavigableMap<K, V> merge(
            PeekingIterator<Entry<K, V>> itThis,
            PeekingIterator<Entry<K, V>> itOther,
            Comparator<? super K> comparator,
            BinaryOperator<V> op,
            K origin,
            V zero) {

        NavigableMap<K, V> merged = new TreeMap<>();
        // Continue iterating while both peeking iterators are not ended
        while (itThis.hasNext() && itOther.hasNext()) {
            MergeParameters<K, V> mergeParameters = new MergeParameters<>(origin, zero, zero);
            mergeParameters = stepParameters(itThis, itOther, comparator, mergeParameters);
            V value = op.apply(mergeParameters.x(), mergeParameters.y());
            merged.put(mergeParameters.index(), value);
        }

        //Iterate remaining values when opposite iterator is done
        while (itThis.hasNext()){
            Entry<K,V> thisEntry = itThis.next();
            merged.put(thisEntry.getKey(), op.apply(thisEntry.getValue(), zero));
        }
        while (itOther.hasNext()){
            Entry<K,V> otherEntry = itOther.next();
            merged.put(otherEntry.getKey(), op.apply(otherEntry.getValue(), zero));
        }
        return merged;
    }

    
    /** 
     * Determine which parameters to merge based on the following values in the iterator
     * 
     * @param itThis
     * @param itOther
     * @param comparator
     * @param mergeParameters
     * @return MergeParameters<K, V>
     */
    private static <K,V> MergeParameters<K,V> stepParameters(
        PeekingIterator<Entry<K, V>> itThis,
        PeekingIterator<Entry<K, V>> itOther,
        Comparator<? super K> comparator,
        MergeParameters<K, V> mergeParameters){

            Function<Entry<K, V>, MergeParameters<K, V>> parameters = entry -> mergeParameters.setX(entry);

            Entry<K,V> entryThis = itThis.peek().get();
            Entry<K,V> entryOther = itOther.peek().get();

            int comparison = comparator.compare(entryThis.getKey(), entryOther.getKey());

            // Conndition where the two indices are the same, then this and other are the parameters
            if(comparison == 0){
                return mergeParameters.setX(itThis.next()).setY(itOther.next());
            }
            
            // Next two blocks show when one of the indicies are greater, then step the value of the lower parameter
            else if (comparison < 0){
                return stepParameters(itThis, parameters);
            }
            else{
                return stepParameters(itOther, parameters);
            }
            
    }

    
    /** 
     * Advance the specified iterator, as called by the previous stepParameters method
     * 
     * @param iterator
     * @param parameters
     * @return MergeParameters<K, V>
     */
    private static <K,V> MergeParameters<K,V> stepParameters(
        PeekingIterator <Entry<K, V>> iterator,
        Function<Entry<K, V>, MergeParameters<K, V>> parameters){
            return parameters.apply(iterator.next());
        }

}
