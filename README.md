![visitor badge](https://vbr.wocr.tk/badge?page_id=isaaclo97.Generalized-Dispersion-Problem&color=be54c6&style=flat&logo=Github)
![Manintained](https://img.shields.io/badge/Maintained%3F-yes-green.svg)
![GitHub last commit (master)](https://img.shields.io/github/last-commit/isaaclo97/Generalized-Dispersion-Problem)
![Starts](https://img.shields.io/github/stars/isaaclo97/Generalized-Dispersion-Problem.svg)

# Max-Min Dispersion with Capacity and Cost for a Practical Location Problem

Diversity and dispersion problems deal with selecting a subset of elements from a given set in such a way that their diversity is maximized. This study considers a practical location problem recently proposed in the context of max-min dispersion models. It is called the generalized dispersion problem, and it models realistic applications by introducing capacity and cost constraints. We propose two effective linear formulations for this problem, and develop a hybrid metaheuristic algorithm based on the variable neighborhood search methodology, to solve real instances.  Extensive numerical computational experiments are performed to compare our hybrid metaheuristic with the state-of-art heuristic, and with integer linear programming formulations (ILP).  Results on public benchmark instances show the superiority of our proposal with respect to the previous algorithms. Our extensive experimentation reveals that ILP models are able to optimally solve medium-size instances with the Gurobi optimizer, although metaheuristics outperform ILP both in running time and quality in large-size instances. 

* Paper link: <https://doi.org/10.1016/j.eswa.2022.116899>
* Impact Factor: 8.500 
* Quartil: Q1 - 22/145 - Computer Science, Artificial Intelligence | Q1 - 23/275 - Engineering, Electrical & Electronic | 2022  <br>
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
@article{LozanoOsorio2022,
  doi = {10.1016/j.eswa.2022.116899},
  url = {https://doi.org/10.1016/j.eswa.2022.116899},
  year = {2022},
  month = aug,
  publisher = {Elsevier {BV}},
  volume = {200},
  pages = {116899},
  author = {Isaac Lozano-Osorio and Anna Mart{\'{\i}}nez-Gavara and Rafael Mart{\'{\i}} and Abraham Duarte},
  title = {Max{\textendash}min dispersion with capacity and cost for a practical location problem},
  journal = {Expert Systems with Applications}
}
```

MDPI and ACS Style
```
Lozano-Osorio, I.; Martínez-Gavara, A.; Martí, R.; Duarte, A. Max–Min Dispersion with Capacity and Cost for a Practical Location Problem. Expert Systems with Applications, 2022, 200, 116899. https://doi.org/10.1016/j.eswa.2022.116899.
```

AMA Style
```
Lozano-Osorio I, Martínez-Gavara A, Martí R, Duarte A. Max–min dispersion with capacity and cost for a practical location problem. Expert Systems with Applications. 2022;200:116899.
```

Chicago/Turabian Style
```
Lozano-Osorio, Isaac, Anna Martínez-Gavara, Rafael Martí, and Abraham Duarte. “Max–Min Dispersion with Capacity and Cost for a Practical Location Problem.” Expert Systems with Applications. Elsevier BV, August 2022. https://doi.org/10.1016/j.eswa.2022.116899.
```
