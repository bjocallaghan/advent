(ns net.bjoc.advent.year-2020.day-20
  (:use [net.bjoc.advent.util.matrix :only [flip rotate]])
  (:require [clojure.string :as str]))

(defn lines->tile [lines]
  (let [size (-> lines second count)]
    {:tile-id (Integer/parseInt (second (re-find #"Tile (\d+):" (first lines))))
     :size size
     :matrix (into {} (for [col (range size)
                            row (range size)]
                        [[col row] (nth (nth lines (inc row)) col)]))}))

(defn dump-tile [{:keys [tile-id matrix size] :as tile}]
  (println (format "Tile %s:" tile-id))
  (doseq [row (range size)]
    (println (str/join (map #(matrix [% row]) (range size))))))

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
                        (rotate num-rotations)
                        (flip num-flips))]
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

(defn part-2 []
  (println
   "Day 20 - Part 2 - asdf asdf asdf:"
   ))
