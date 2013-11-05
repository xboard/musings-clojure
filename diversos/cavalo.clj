;;
;; 1) Tornar a solução mais genérica, para tamanhos de tabuleiro
;; diferentes, passados como parâmetro.
;; 2) Melhorar nome de cada uma das funções.
;; 3) Melhorar as implementações (high-order algumas funs?)
;;

(def *N* 8)


(defn pos2vec
  [num]
  (cond
   (<  num 0) nil
   (>= num (* *N* *N*)) nil
   :else (vector (int (/ num *N*)) (rem num *N*)))
  )


(defn vec2pos
  [v]
  ;;(println "--" v "--")
  (cond (vector? v) (let [[x y] v] (+(* x *N*) y))
        (seq? v) (let [x (first v) y (last v)]  (+(* x *N*) y))
        :else nil)
)

(defn pos-valida?
  [pos]
  (if (nil? pos) false
      (let [x (first pos) y (last pos)]
        (cond
         (or (>= x *N*)(< x 0)) false
         (or (>= y *N*)(< y 0)) false
         :else true))))

(defn init-board
  "Devolve valores iniciais como um vetor de inteiros, cada inteiro representa o número de movimentos na iteração."
  ([pos] (init-board (first (pos2vec pos)) (last (pos2vec pos))))
  ([x y]
  (assoc (vec(take (* *N* *N*)(cycle [0]))) (+(* *N* x) y) 1)))


(defn get-candidate-moves
  [pos]
  (map #(map + %1 %2) (cycle (list (pos2vec pos))) [[ 1  2][ 1 -2][-1  2][-1 -2]
                                                            [ 2  1][ 2 -1][-2  1][-2 -1]]))

(defn get-moves
  "Devolve as posições de destino, dada uma origem"
  [pos]
  (if (not(pos-valida? (pos2vec pos))) '() ;;posição inicial inválida
       (filter pos-valida? (get-candidate-moves pos))))

(defn get-dep-values
  "Devolve lista com os valores nas posições de onde pode ter chegado em pos"
  ;;(get-dep-values 10 (init-board 0 0))
  [pos board]
  (map #(get board %) (map vec2pos(get-moves pos))))

(defn set-dep-value
  "Insere um valor no board"
  [board pos value]
  (assoc board pos value))


(defn computa-pos
  ;; ex: (computa-pos (init-board 0 0))
  ([board] (computa-pos 0 board board))
  ([pos board] (computa-pos pos board board))
  ([pos board old-board] ;;(println pos)
     (if (not(pos-valida? (pos2vec pos))) board
        (recur (inc pos) (assoc board pos (reduce + (get-dep-values pos old-board))) old-board)
     )
   )
 )

(defn result
  ;;
  ([pos-inicial pos-final iter] (result pos-inicial pos-final iter 0 (init-board pos-inicial) ))
  ([pos-inicial pos-final iter i board]
     ;;(println i board)
     (cond (>= i iter) (get board pos-final)
           :else (recur pos-inicial pos-final iter (inc i) (computa-pos pos-inicial board)))
     )
  )
