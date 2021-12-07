(ns net.bjoc.advent.year-2020.day-20
  (:use [clojure.set :only [intersection]]
        [net.bjoc.advent.util.misc :only [find-first single]])
  (:require [clojure.string :as str]
            [net.bjoc.advent.util.matrix :as mtx]))

(defn lines->tile [lines]
  (let [size (-> lines second count)]
    {:tile-id (Integer/parseInt (second (re-find #"Tile (\d+):" (first lines))))
     :size size
     :matrix (mtx/from-string (str/join "\n" (rest lines)))}))

(defn file->tiles [filename]
  (with-open [rdr (clojure.java.io/reader filename)]
    (loop [lines (line-seq rdr)
           line nil
           acc nil
           tiles []]
      (if (empty? lines)
        (if acc (conj tiles (lines->tile acc)) tiles)
        (recur (rest lines)
               (first lines)
               (if (empty? line) [] (conj acc line))
               (if (and acc (empty? line))
                 (conj tiles (lines->tile acc))
                 tiles))))))

(defn tile->edges [{:keys [tile-id matrix size] :as tile}]
  (for [num-flips (range 2)
        num-rotations (range 4)]
    (let [transform (-> matrix
                        (mtx/rotate num-rotations)
                        (mtx/flip num-flips))]
      {:tile-id tile-id
       :num-flips num-flips
       :num-rotations num-rotations
       :text (str/join (map #(transform [% 0]) (range size)))})))

(defn compatible-edge-pair? [[edge-1 edge-2]]
  (= (:text edge-1) (:text edge-2)))

(defn possible-neighbors [edges tile]
  (let [edge-1? #(= (:tile-id tile) (:tile-id %))
        not-edge-1? (complement edge-1?)]
    (->> (for [edge-1 edges
               edge-2 edges
               :when (and (edge-1? edge-1)
                          (not-edge-1? edge-2))]
           [edge-1 edge-2])
         (filter compatible-edge-pair?)
         (map second)
         (map :tile-id)
         (into #{}))))

(defn definite-corners [tiles]
  (let [edges (mapcat tile->edges tiles)
        possible-neighbors* (partial possible-neighbors edges)]
    (filter #(= 2 (count (possible-neighbors* %))) tiles)))

(defn file->product-of-corners [filename]
  (->> (file->tiles filename)
       (definite-corners)
       (map :tile-id)
       (reduce *)))

(defn part-1 []
  (println
   "Day 20 - Part 1 - Once solved, multiply the product of corner IDs:"
   (file->product-of-corners "data/year_2020/day_20.input")))

;;;

(def monster
  (->> (mtx/from-string (slurp "data/year_2020/day_20_supplemental.input")
                        :type :characters)
       (remove (fn [[_ v]] (= v \space)))
       (into {})))

(defn monster? [matrix [x y]]
  (every? (fn [[monster-x monster-y]]
            (= \# (matrix [(+ x monster-x) (+ y monster-y)])))
          (keys monster)))

(defn mark-monsters [matrix]
  (let [mark-monster (fn [matrix [x y]]
                       (if (monster? matrix [x y])
                         (reduce (fn [m [monster-x monster-y]]
                                   (assoc m [(+ x monster-x) (+ y monster-y)] \O))
                                 matrix (keys monster))
                         matrix))]
    (reduce mark-monster matrix (keys matrix))))

(defn rough-water-score [matrix]
  (let [score (fn [m] (->> (vals m)
                           (filter #(= % \#))
                           count))
        ms (for [num-rotations (range 4)
                 num-flips (range 2)]
             (->> matrix
                  (#(mtx/rotate % num-rotations))
                  (#(mtx/flip % num-flips))))]
    (->> ms
         (map mark-monsters)
         (map score)
         (apply min))))

(defn fill-order [size]
  (let [in? (fn [[x y]] (and (<= 0 x (dec size)) (<= 0 y (dec size))))]
    (->> (for [x (range (+ size (dec size)))
               y (concat (range 1 size) [0])]
           [(- x y) y])
         (filter in?))))

(defn assemble*
  "Return a matrix of tiles. However, the tiles won't be properly rotated yet."
  [tiles]
  (let [size (int (Math/sqrt (count tiles)))
        edges (mapcat tile->edges tiles)
        neighbors* (partial possible-neighbors edges)
        tiles (map #(assoc % :neighbors (neighbors* %)) tiles)
        find-tile (fn [id] (find-first #(= (:tile-id %) id) tiles))
        rem-n (fn remove-neighbor [tile neighbor-tile-id & second-id]
                (-> tile
                    (update :neighbors disj neighbor-tile-id)
                    (update :neighbors disj (first second-id))))
        corner (->> tiles
                    (filter #(= 2 (count (:neighbors %))))
                    first)
        arbitrary (-> corner :neighbors first find-tile)
        layout {[0 0] (rem-n corner (:tile-id arbitrary))
                [0 1] (rem-n arbitrary (:tile-id corner))}]
    (reduce (fn [m [x y]]
              (let [up (m [x (dec y)])
                    left (m [(dec x) y])
                    new-id (single
                            (cond
                              (and up left) (intersection (:neighbors up)
                                                          (:neighbors left))
                              up (-> up :neighbors)
                              left (-> left :neighbors)))
                    new (rem-n (find-tile new-id) (:tile-id up) (:tile-id left))
                    new-m (assoc m [x y] new)]
                (cond
                  (and up left) (-> new-m
                                    (update [(dec x) y] #(rem-n % new-id))
                                    (update [x (dec y)] #(rem-n % new-id)))
                  up (update new-m [x (dec y)] #(rem-n % new-id))
                  left (update new-m [(dec x) y] #(rem-n % new-id)))))
            layout (drop 2 (fill-order size)))))

(defn edge-text [{:keys [matrix size] :as tile} side]
  (str/join
   (map (fn [i]
          (matrix
           (case side
             :left [0 i]
             :right [(dec size) i]
             :up [i 0]
             :down [i (dec size)])))
        (range size))))

(defn all-tile-xforms [tile]
  (for [num-rotations (range 4)
        num-flips (range 2)]
    (assoc tile :matrix
           (-> (:matrix tile)
               (mtx/rotate num-rotations)
               (mtx/flip num-flips)))))

(defn align-tile [text side {:keys [matrix] :as tile}]
  (find-first #(= text (edge-text % side))
              (all-tile-xforms tile)))

(defn assemble**
  "Rotate tile [0 0] into a proper alignment."
  [tiles]
  (let [layout (assemble* tiles)
        corner (layout [0 0])
        right (layout [1 0])
        down (layout [0 1])
        right-edges (->> (tile->edges right) (map :text) (into #{}))
        down-edges (->> (tile->edges down) (map :text) (into #{}))]
    (assoc layout [0 0]
           (find-first (fn [t]
                         (and (right-edges (edge-text t :right))
                              (down-edges (edge-text t :down))))
                       (all-tile-xforms corner)))))

(defn assemble***
  "Rotate the remainder of the layout tiles into proper alignment.

  Now that tile [0 0] and [1 0] are fixed, all the other tiles 'naturally' lock
  into place."
  [tiles]
  (let [size (int (Math/sqrt (count tiles)))
        layout (assemble** tiles)]
    (reduce
     (fn [m [x y]]
       (let [tile (layout [x y])
             [other-tile other-side this-side] (if (layout [x (dec y)])
                                                 [(m [x (dec y)]) :down :up]
                                                 [(m [(dec x) y]) :right :left])
             text (edge-text other-tile other-side)]
         (assoc m [x y] (align-tile text this-side tile))))
     layout (drop 1 (fill-order size)))))

(defn assemble
  "Remove the border alignment meta-info from a layout and stitch together a
  layout's individual matrices into one large matrix."
  [tiles]
  (let [layout (assemble*** tiles)
        inner-size (-> (layout [0 0]) :matrix mtx/size first dec dec)
        outer-size (->> layout keys (apply concat) (apply max) inc)]
    (into {}
          (for [inner-x (range inner-size)
                inner-y (range inner-size)
                outer-x (range outer-size)
                outer-y (range outer-size)]
            [[(+ (* outer-x inner-size) inner-x)
              (+ (* outer-y inner-size) inner-y)]
             (-> (layout [outer-x outer-y])
                 :matrix
                 (get [(inc inner-x) (inc inner-y)] [inner-x inner-y]))]))))

(defn file->rough-water-score [filename]
  (-> filename file->tiles assemble rough-water-score))

(defn part-2 []
  (println
   "Day 20 - Part 2 - Rough water score of assembled map:"
   (file->rough-water-score "data/year_2020/day_20.input")))
