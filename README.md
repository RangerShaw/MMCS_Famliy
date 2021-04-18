# MMCS Algorithm Family in Java

## Intro

This repo includes different MMCS algorithms implemented in Java for discovering minimal hitting sets.

Currently it includes:

+ **MMCS**: Original MMCS. Located in algorithm/MMCS

+ **EMMCS**: Extendable MMCS that supports inserting new subsets that should be covered. Located in algorithm/EMMCS

+ **BHMMCS**: Bi-directional Hybrid MMCS that supports both inserting and removing subsets. Removing is implemented by
  re-initiating from the remained subset, which is currently the fastest method. Located in algorithm/BHMMCS

Additionally, it includes corresponding FD Connectors for each MMCS algorithm (located in algorithm/fdConnectors), which
provide convenience for discovering minimal FDs.

## How to Use

### MMCS

```java
    Mmcs mmcs = new Mmcs(nElements);    // nElements is the number of total attributes of input dataset
    mmcs.initiate(diffSets);            // diffSets, in form List<BitSet>, represents subsets to be covered
    List<BitSet> minCoverSets = mmcs.getMinCoverSets();     // retrieve the resulted minimal hitting sets

```

### EMMCS

```java
    Emmcs emmcs = new Emmcs(nElements);
    emmcs.initiate(diffSets);
    List<BitSet> minCoverSets0 = mmcs.getMinCoverSets();

    emmcs.insertSubsets(insertedSets1);     // insertedSets, in form List<BitSet>,
    emmcs.insertSubsets(insertedSets2);     // represents newly inserted subsets to be covered
    ...
    List<BitSet> minCoverSets1 = emmcs.getMinCoverSets();   // retrieve the resulted minimal hitting sets

```

### BHMMCS

```java
    Bhmmcs bhmmcs = new Bhmmcs(nElements);
    bhmmcs.initiate(diffSets);
    List<BitSet> minCoverSets0 = bhmmcs.getMinCoverSets();  // optional

    bhmmcs.insertSubsets(insertedSets1);
    bhmmcs.removeSubsets(remainedSets2);     // remainedSets2 represents remained subsets after removal
    ...
    List<BitSet> minCoverSets1 = bhmmcs.getMinCoverSets();  // retrieve the resulted minimal hitting sets

```

### FdConnector

Basically the same as their corresponding MMCS algorithms. The only difference is that an FdConnector will successively
call its MMCS algorithm on each RHS and return a list of minimal FDs on all RHSs.

Take BhmmcsFdConnector for example:

```java
    BhmmcsFdConnector bhmmcsFdConnector = new BhmmcsFdConnector(nElements); 
    bhmmcsFdConnector.initiate(diffSets);
    List<BitSet> minCoverSets0 = bhmmcsFdConnector.getMinCoverSets();

    bhmmcsFdConnector.insertSubsets(insertedSets1);    
    bhmmcsFdConnector.removeSubsets(remainedSets2);     // remainedSets2 represents remained subsets after removal
    ...
    List<BitSet> minCoverSets1 = bhmmcsFdConnector.getMinCoverSets();   // retrieve the resulted minimal hitting sets
}
```