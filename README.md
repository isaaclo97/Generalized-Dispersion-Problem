# Max-Min Dispersion with Capacity and Cost for a Practical Location Problem

Diversity and dispersion problems deal with selecting a subset of elements from a given set in such a way that their diversity is maximized. This study considers a practical location problem recently proposed in the context of max-min dispersion models. It is called the generalized dispersion problem, and it models realistic applications by introducing capacity and cost constraints. We propose two effective linear formulations for this problem, and develop a hybrid metaheuristic algorithm based on the variable neighborhood search methodology, to solve real instances.  Extensive numerical computational experiments are performed to compare our hybrid metaheuristic with the state-of-art heuristic, and with integer linear programming formulations (ILP).  Results on public benchmark instances show the superiority of our proposal with respect to the previous algorithms. Our extensive experimentation reveals that ILP models are able to optimally solve medium-size instances with the Gurobi optimizer, although metaheuristics outperform ILP both in running time and quality in large-size instances. 

* Paper link: <>
* Impact Factor: 6.954 
* Quartil: Q1
* Journal: Expert Systems with Applications

![USCAP](https://user-images.githubusercontent.com/20272434/158191813-5efc0a4b-2fdf-4109-aacb-ad31a575a7c7.png)

## Datasets

* [GDP Instances](/instances/GDP)
* [USCAP real instance](/instances/USCAP)


All txt format instances can be found in instances folder.

## Executable

You can just run the GDP.jar as follows.

```
java -jar GDP.jar "FOLDER_WITH_INSTANCES"
```

```
java -jar GDP.jar instances/USCAP
```

If you want new instances just replace folder instances.

## Solutions

Solution folder contains an excel with the results and a folder with each solution per instance.

## Cite

Please cite our paper if you use it in your own work:

Bibtext
```
```

MDPI and ACS Style
```
```

AMA Style
```
```

Chicago/Turabian Style
```
```
