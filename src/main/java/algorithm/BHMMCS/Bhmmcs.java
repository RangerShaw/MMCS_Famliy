package algorithm.BHMMCS;

import algorithm.Subset;
import util.Utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Bidirectional Hybrid MMCS algorithm
 * that supports inserting and deleting difference sets
 */
public class Bhmmcs {

    /**
     * number of elements or attributes
     */
    private int nElements;

    /**
     * each node represents a minimal cover set
     */
    private List<BhmmcsNode> coverNodes = new ArrayList<>();

    /**
     * true iff there's an empty subset to cover (which could never be covered).
     * return no cover set if true but walk down without the empty subset
     */
    private boolean hasEmptySubset = false;

    /**
     * record nodes walked to avoid duplication
     */
    private Set<BitSet> walked = new HashSet<>();


    public Bhmmcs(int nEle) {
        nElements = nEle;
    }

    /**
     * @param setsToCover unique BitSets representing Subsets to be covered
     */
    public void initiate(List<BitSet> setsToCover) {
        coverNodes = new ArrayList<>();

        hasEmptySubset = setsToCover.stream().anyMatch(BitSet::isEmpty);

        List<Subset> subsets = setsToCover.stream().filter(bs -> !bs.isEmpty()).map(Subset::new).collect(Collectors.toList());

        BhmmcsNode initNode = new BhmmcsNode(nElements, subsets);

        walkDown(initNode, coverNodes);

        // System.out.println("# of walked: " + walked.size());
    }

    /**
     * @param insertedSets unique BitSets representing newly inserted Subsets to be covered
     */
    public void insertSubsets1(List<BitSet> insertedSets) {
        walked.clear();

        hasEmptySubset |= insertedSets.stream().anyMatch(BitSet::isEmpty);

        List<Subset> insertedSubsets = insertedSets.stream().filter(bs -> !bs.isEmpty()).map(Subset::new).collect(Collectors.toList());

        long startTime1 = System.nanoTime();
        for (BhmmcsNode prevNode : coverNodes)
            prevNode.insertSubsets(insertedSubsets);
        long endTime1 = System.nanoTime();
        System.out.println("runtime 1 of ADD: " + (endTime1 - startTime1) / 1000000 + "ms");

        long startTime2 = System.nanoTime();
        List<BhmmcsNode> newCoverSets = new ArrayList<>();
        for (BhmmcsNode prevNode : coverNodes)
            walkDown(prevNode, newCoverSets);
        long endTime2 = System.nanoTime();
        System.out.println("runtime 2 of ADD: " + (endTime2 - startTime2) / 1000000 + "ms");

        coverNodes = newCoverSets;

        System.out.println("# of walked: " + walked.size());
    }

    public void insertSubsets(List<BitSet> insertedSets) {
        walked.clear();

        hasEmptySubset |= insertedSets.stream().anyMatch(BitSet::isEmpty);

        List<Subset> insertedSubsets = insertedSets.stream().filter(bs -> !bs.isEmpty()).map(Subset::new).collect(Collectors.toList());

        List<BhmmcsNode> newCoverSets = new ArrayList<>();
        for (BhmmcsNode prevNode : coverNodes) {
            prevNode.insertSubsets(insertedSubsets);
            walkDown(prevNode, newCoverSets);
        }

        coverNodes = newCoverSets;
    }

    /**
     * down from nd on the search tree, find all minimal hitting sets
     */
    void walkDown(BhmmcsNode nd, List<BhmmcsNode> newNodes) {
        if (walked.contains(nd.getElements())) return;
        walked.add(nd.getElements());

        if (nd.isCover()) {
            newNodes.add(nd);
            return;
        }

        // configure cand for child nodes
        BitSet childCand = nd.getCand();
        BitSet addCandidates = nd.getAddCandidates();
        childCand.andNot(addCandidates);

        addCandidates.stream().forEach(e -> {
            BhmmcsNode childNode = nd.getChildNode(e, childCand);
            if (childNode.isGlobalMinimal()) {
                walkDown(childNode, newNodes);
                childCand.set(e);
            }
        });
    }

    /**
     * @param remainedSets unique BitSets representing remained Subsets after removal
     */
    public void removeSubsets(List<BitSet> remainedSets) {
        walked.clear();

        initiate(remainedSets);
    }

    public List<BitSet> getMinCoverSets() {
        return hasEmptySubset ? new ArrayList<>() : coverNodes.stream()
                .map(BhmmcsNode::getElements)
                .sorted(Utils.BitsetComparator())
                .collect(Collectors.toList());
    }

}
