
(defn func-a [x f]
  (f (inc x)))


(defn func-b [x f]
  (f (* 2 x)))


(defn func-c [x f]
  (f (dec x)))

(defn cont-b [x]
  (func-c x identity))


(defn cont-a [x]
  (func-b x cont-b))


(defn fn2 [x]
  (let [a (func-a x)
        c (func-b a func-c)]
    c))

(defn fn3 [x]
  (func-a x cont-a))

(defn fn4 [x]
  (func-a x cont-a))

(defn fn5 [x f]
  (f (func-a x cont-a)))

(defn mf-a [x]
  (fn [c]
    (c (inc x))))

(defn mf-b [x]
  (fn [c]
    (c (* 2 x))))

(defn mf-c [x]
  (fn [c]
    (c (dec x))))

(defn m-result-cont [v]
              (fn [c]
                (c v)))
(defn m-bind-cont [mv mf]
             (fn [c]
               (mv (fn [v]
                     ((mf v) c)))))
