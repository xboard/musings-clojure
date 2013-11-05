(defn longer? [x y]
  (let [cmp (fn [x y]
              (and (empty? x)
                   (or (empty? y)
                       (recur (rest x) (rest y)))))]
    (if (and (seq x) (seq y))
      (cmp x y)
      (> (count x) (count y)))))


(defn filter2
  "filtra os elementos de uma lista"
  [f lst]
  nil
  )

(defn group
  "Agrupa uma lista em sublistas de até n elementos"
  [source n]
  '()
  )

(defn filter-tree
  "like list filters but over a tree"
  [f tree]
  (if(not(fn? f))
    tree
    '()))


(defn mcycle
  "reimplementação de clojure cycle"
  [lst]
  
  )
