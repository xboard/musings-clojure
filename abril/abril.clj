(import '(java.io BufferedReader FileReader))

(defn get-digits
  "Recebe um número e devolve seus dígitos como uma lista"
  [x]
  (if (< x 1)
    '()
    (concat [(int (rem x 10))]
           (get-digits (/ x 10)))
      )
  )

(defn soma-quadrados
  "Devolve a soma dos quadrados dos números em uma lista"
  [numeros]
  (reduce + (map #(* % %) numeros))
  )


(defn problema-1
  "solução para o primeiro problema"
  ([x] (problema-1 x #{}))
  ([x previous]
   (let [num (soma-quadrados (get-digits x))]
     ;;(println "x=" x "num=" num "previous=" previous)
      (if (= num 1) 1
       (if (contains? previous num) 0
           (problema-1 num (conj previous x))))
   )
  )
)


(defn resolve-problemas-arquivo
  "Abre o arquivo e manda resolver problema para cada linha"
  [file-name]
  (with-open [rdr (BufferedReader. (FileReader. file-name))]
    (doseq [line (line-seq rdr)] (println (problema-1 (Integer/parseInt line)))))
)


(resolve-problemas-arquivo (first *command-line-args*))

