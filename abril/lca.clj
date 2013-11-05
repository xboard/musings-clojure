(def tree {10 20, 29 20, 3 8, 20 8, 8 30, 52 30})



(defn ancestors
  "Devolve um set com os ancestrais desse número na árvore"
  [x]
  (let [parent (tree x)]
    (if (nil? parent) '() (cons parent (get-ancestors parent)))
    )
)

(defn achou?
  "Devolve true se x está na lista list"
  [x list]
  (not(empty? (filter #(= %1 x) list)))
)  

(defn intersect
  "Devolve o primeiro elemento de interseção de duas listas (l -> r)"
  [a b]
  (loop [x a acc '()]
    ;;(println (first x))
    (let [n (first x)]
    (if (nil? n) nil
        (if (achou? n b) n
            (recur (rest x) acc)
        )
    )
   )
  )
)
  

(defn solve-problem-2
  "Resolve o segundo problema"
  [x y]
  (intersect (ancestors x) (ancestors y))
)

(println (solve-problem-2 3 29))
(println (solve-problem-2 8 52))
