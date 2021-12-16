(ns net.bjoc.advent.year-2021.day-15
  (:use [net.bjoc.advent.util.numeric :only [parse-int]]
        [net.bjoc.advent.util.coords :only [add-coords]])
  (:require [net.bjoc.advent.util.matrix :as mtx]
            [net.bjoc.advent.core :as advent]))

(defn file->cavern [filename]
  (->> filename
       slurp
       mtx/from-string
       (map (fn [[k v]] [k (parse-int (str v))]))
       (into {})))

(defn neighbors-fn [m]
  (let [[x-min y-min] [0 0]
        [x-max y-max] (map dec (mtx/size m))
        in-bounds? (fn [[x y]] (and (<= x-min x x-max) (<= y-min y y-max)))
        offsets [[1 0] [0 1] [-1 0] [0 -1]]]
    (fn [xy]
      (->> (map (partial add-coords xy) offsets)
           (filter in-bounds?)))))

(defn danger-matrix
  "Produce the 'danger matrix' from a given cavern.

  Each element in the matrix is the sum of the lowest-danger path to the goal
  from this point.

  This implementation is algorithmically sound, but has a TON of redundant
  calculation, which makes it non-performant for larger matrices (took about 10
  minutes for 100x100)."
  [m]
  (let [neighbors (neighbors-fn m)
        candidates (fn [dm]
                     (apply concat
                            (for [[xy danger] dm]
                              (let [unocc-neighbors (->> (neighbors xy) (remove dm))]
                                (map (fn [n-xy] [n-xy (+ danger (m n-xy))])
                                     unocc-neighbors)))))
        dest-corner-xy (vec (map dec (mtx/size m)))]
    (loop [dm {dest-corner-xy (m dest-corner-xy)}]
      (if-let [cands (not-empty (candidates dm))]
        (recur (conj dm (first (sort-by second cands))))
        dm))))

(defn danger-matrix*
  "Produce the 'danger matrix' from a given cavern.

  Each element in the matrix is the sum of the lowest-danger path to the goal
  from this point.

  Improved performance, but I haven't attempted to fully optimize. Takes about 5
  minutes for a 500x500."
  [m]
  (let [neighbors (neighbors-fn m)
        dest-corner-xy (vec (map dec (mtx/size m)))
        initial-frontier #{[dest-corner-xy (m dest-corner-xy)]}
        remove* (fn [frontier xy]
                  (reduce (fn [frontier v] (disj frontier v))
                          frontier 
                          (filter (fn [[f-xy _]] (= f-xy xy)) frontier)))]
    (loop [dm {}
           frontier initial-frontier]
      (if (empty? frontier)
        dm
        (let [best (first (sort-by second frontier))
              new-frontier-vals (->> (first best)
                                     neighbors
                                     (remove dm)
                                     (map (fn [xy] [xy (+ (m xy) (second best))])))]
          (recur (conj dm best)
                 (reduce conj (remove* frontier (first best)) new-frontier-vals)))))))

(defn file->lowest-danger
  ([filename] (file->lowest-danger filename danger-matrix*))
  ([filename dm-fn]
   (let [m (file->cavern filename)
         dm (dm-fn m)]
     (- (get dm [0 0]) (get m [0 0])))))

;;;

(defn expand [m]
  (let [[x-size y-size] (mtx/size m)
        not-zero? #(if (zero? %) nil %)]
    (into {}
          (for [x-outer (range 5)
                y-outer (range 5)
                [[x-inner y-inner] v] m]
            [[(+ x-inner (* x-outer x-size))
              (+ y-inner (* y-outer y-size))]
             (if-let [v (not-zero? (mod (+ (m [x-inner y-inner]) x-outer y-outer) 9))]
               v 9)]))))

(defn file->cavern* [filename]
  (expand (file->cavern filename)))

(defn file->lowest-danger*
  ([filename] (file->lowest-danger* filename danger-matrix*))
  ([filename dm-fn]
   (let [m (file->cavern* filename)
         dm (dm-fn m)]
     (- (get dm [0 0]) (get m [0 0])))))

;;;

(advent/defrunner 1 file->lowest-danger "Lowest danger")
(advent/defrunner 2 file->lowest-danger* "Lowest danger (expanded)")
