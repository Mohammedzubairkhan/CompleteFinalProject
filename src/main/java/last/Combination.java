package last;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Combination<T> {

    
    public static void main(String[] args){
        //Object[] objects = new Object[] {0,1,3,4};
        
        List<Integer> elements = new ArrayList<Integer>();
        elements.add(0);
        elements.add(1);
        elements.add(3);
        elements.add(4);
        
        Combination c = new Combination();
        Set<List<Integer>> sets = c.combination(elements,2);
        
        
        Iterator<List<Integer>> i = sets.iterator();
        // Display elements
        while (i.hasNext()) {
            System.out.println(i.next());
        }
    }
    
    
    public Set<List<T>> combination(List<T>  elements, int K){

        Set<List<T>> sets = new HashSet<List<T>>();

        
        // get the length of the array
        // e.g. for {'A','B','C','D'} => N = 4 
        int N = elements.size();
        
        if(K > N){
            System.out.println("Invalid input, K > N");
            return sets;
        }
        // calculate the possible combinations
        // e.g. c(4,2)
        //c(N,K);
        
        // get the combination by index 
        // e.g. 01 --> AB , 23 --> CD
        int combination[] = new int[K];
        
        // position of current index
        //  if (r = 1)                r*
        //    index ==>        0    |    1    |    2
        //    element ==>        A    |    B    |    C
        int r = 0;        
        int index = 0;
        
        while(r >= 0){
            // possible indexes for 1st position "r=0" are "0,1,2" --> "A,B,C"
            // possible indexes for 2nd position "r=1" are "1,2,3" --> "B,C,D"
            
            // for r = 0 ==> index < (4+ (0 - 2)) = 2
            if(index <= (N + (r - K))){
                    combination[r] = index;
                    
                // if we are at the last position print and increase the index
                if(r == K-1){

                    //do something with the combination e.g. add to list or print
                    //print(combination, elements);
                    List<T> list= getCombination(combination,elements);
                    sets.add(list);
                    
                    index++;                
                }
                else{
                    // select index for next position
                    index = combination[r]+1;
                    r++;                                        
                }
            }
            else{
                r--;
                if(r > 0)
                    index = combination[r]+1;
                else
                    index = combination[0]+1;    
            }            
        }
        return sets;
    }
    

    
    private List<T> getCombination(int[] combination, List<T> elements) {
        List<T> arrays = new ArrayList<T>();
        for(int z = 0 ; z < combination.length;z++){
            arrays.add(elements.get(combination[z]));
        }
        //System.out.println(arrays);
        return arrays;
    }


    public static int c(int n, int r){
        int nf=fact(n);
        int rf=fact(r);
        int nrf=fact(n-r);
        int npr=nf/nrf;
        int ncr=npr/rf; 
        
        System.out.println("C("+n+","+r+") = "+ ncr);

        return ncr;
    }
    
    public static int fact(int n)
    {
        if(n == 0)
            return 1;
        else
            return n * fact(n-1);
    }
    
}
