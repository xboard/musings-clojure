

(defn solve
  "Find one instance solution"
  ([x] (solve x 1 #{}))
  ([x i solutions]
     ;;(println x i solutions)
     (let [a (Math/sqrt (- x (* i i)))]
       ;;(println x i a solutions)
       (if (>= i (int (Math/sqrt x))) solutions
           (if (and (= a (int a)) (> a 0))
             (recur x (+ i 1) (set (cons (sort [i (int a)]) solutions)))
             (recur x (+ i 1) solutions))
            )
        )
    )
);;end of solve

(defn solve4
  "Resolve problema 4"
  [x]
  (count (solve x))
)

(println (solve 25))
