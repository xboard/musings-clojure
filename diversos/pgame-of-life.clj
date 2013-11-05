(def cells (ref {})) 
(def running (ref false)) 
;(def x-cells ( * 32 4)) 
;(def y-cells ( * 48 4)) 
(def x-cells 32) 
(def y-cells 32) 
(def range-cells (for [x (range x-cells) y (range y-cells)] [x y])) 
(def length-range-cells (count range-cells)) 
(def cell-size 10) 
(def life-delay 0) 
(def life-initial-prob 3) 
(def available-procs (.. java.lang.Runtime getRuntime 
availableProcessors)) 
(defn determine-initial-state [x y] 
  (= 0 (rand-int life-initial-prob))) 
(defn determine-new-state [x y] 
  (let [alive (count (for [dx [-1 0 1] dy [-1 0 1] 
                           :when (and (not (= 0 dx dy)) 
                                   (cells [ (mod (+ x dx) x-cells) 
(mod (+ y dy) y-cells)]))] 
                       :alive))] 
    (if (cells [x y]) 
      (< 1 alive 4) 
      (= alive 3)))) 
(defn update-batch-of-new-cells [new-cells list-of-batches] 
  (dosync 
    (dorun (map #(commute new-cells assoc (first %) (second %)) 
             list-of-batches)) 
    )) 
(defn calc-batch-of-new-cell-states [cell-state batch-cells] 
  (doall (map 
           #(let [new-cell-state (cell-state (first %) (second %))] 
              [[(first %) (second %)] new-cell-state]) 
           batch-cells))) 
(defn calc-state [cell-state] 
  (let [new-cells (ref {})] 
    (dorun (pmap #(update-batch-of-new-cells new-cells %) 
             (pmap #(calc-batch-of-new-cell-states cell-state %) 
               (for [cpu (range available-procs)] (take-nth available-procs (drop cpu range-cells)))))) 
    (dosync (ref-set cells @new-cells)))) 
(defn paint-cells [#^java.awt.Graphics graphics] 
  (doseq [[[x,y] state] @cells] 
    (doto graphics 
      (. setColor (if state Color/RED Color/WHITE)) 
      (. fillRect (* cell-size x) (* cell-size y) cell-size cell- 
size)))) 
(defn toggle-thread [#^JPanel panel button] 
  (if @running 
    (do (dosync (ref-set running false)) 
      (. button (setText "Start"))) 
    (do (dosync (ref-set running true)) 
      (. button (setText "Stop")) 
      (. (Thread. 
           #(loop [] 
              (calc-state determine-new-state) 
              (. panel repaint) 
              (if life-delay (Thread/sleep life-delay)) 
              (if @running (recur)))) 
        start)))) 
(defn -main[] 
  (calc-state determine-initial-state) 
  (let [f (JFrame.) 
        b (JButton. "Start") 
        panel (proxy [JPanel] [] (paint [graphics] (paint-cells 
graphics)))] 
    (doto f 
      (. setLayout (BorderLayout.)) 
      (. setLocation 100 100) 
      (. setPreferredSize (Dimension. (* cell-size x-cells) (+ 60 (* 
cell-size y-cells)))) 
      (. add b BorderLayout/SOUTH) 
      (. add panel BorderLayout/CENTER) 
      (. setDefaultCloseOperation JFrame/EXIT_ON_CLOSE) 
      (. pack) 
      (. setVisible true)) 
    (. b addActionListener 
      (proxy [ActionListener] [] 
        (actionPerformed [evt] (toggle-thread panel b))))))
