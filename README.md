# MMCS Algorithm Family in Java

## Intro

This repo includes different MMCS algorithms implemented in Java 
for discovering minimal hitting sets (cover sets).

Currently it includes:
+ Original MMCS (located in algorithm/MMCS) 
+ Extendable MMCS (EMMCS, located in algorithm/EMMCS) which supports inserting new subsets that should be covered

Also it includes corresponding FD Connectors (located in algorithm/fdConnectors), 
which provide convenience for discovering minimal FDs.

## How to Use

### MMCS

```java
    Mmcs mmcs = new Mmcs(nElements);    // nElements is the number of total attributes of input dataset
    mmcs.initiate(diffSets);            // diffSets, in form List<BitSet>, represents subsets to be covered
    List<BitSet> minCoverSets = mmcs.getMinCoverSets();     // retrieve the resulted minimal hitting sets
    
```

### EMMCS

```java
    Emmcs emmcs = new Emmcs(nElements);     // nElements is the number of total attributes of input dataset
    emmcs.initiate(diffSets);               // diffSets, in form List<BitSet>, represents subsets to be covered
    emmcs.insertSubsets(insertedSets);      // insertedSets, in form List<BitSet>, represents inserted subsets to be covered
    List<BitSet> minCoverSets = emmcs.getMinCoverSets();     // retrieve the resulted minimal hitting sets
    
```

### FdConnector

