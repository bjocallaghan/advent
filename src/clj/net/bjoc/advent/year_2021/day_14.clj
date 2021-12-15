(ns net.bjoc.advent.year-2021.day-14
  (:use [net.bjoc.advent.util.misc :only [zip]])
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]))
  
(defn file->chain [filename]
  (-> filename
      slurp
      (str/split #"\n")
      first))

(defn file->rules [filename]
  (let [lines (-> filename
                  slurp
                  (str/split #"\n\n")
                  second
                  (str/split #"\n"))]
    (->> lines
         (map (fn [line]
                (let [[_ pair insert] (re-find #"^(..) -> (.)$" line)]
                  [(-> pair seq vec) (first insert)])))
         (into {}))))

(defn grow-fn [rules]
  (fn grow
    ([chain] (grow chain 1))
    ([chain num-times]
     (if (zero? num-times)
       chain
       (recur (->> (zip chain (drop 1 chain))
                   (map rules)
                   (interleave chain)
                   (#(concat % [(last chain)]))
                   (remove nil?)
                   (apply str))
              (dec num-times))))))

(defn freqs-after-growth
  "Returns the frequencies of elements in a chain after a number of reactions."
  [chain rules num-times]
  (frequencies ((grow-fn rules) chain num-times)))

;;;

(defn inc* [x] (inc (or x 0)))

(defn freqs-after-growth*
  "Returns the frequencies of elements in a chain after a number of reactions.

  I had fun developing this algorithm, and while doing so I /thought/ it would
  result in a performance boost, but even though it doesn't attempt to do the
  whole thing at once, the 'chain' stack just keeps growing. So ultimately, this
  function isn't much more useful than the first."
  [chain rules num-times]
  (loop [curr (first chain)
         chain (rest (seq chain))
         freqs {curr 1}
         num-times num-times
         num-times-stack (repeat (count chain) num-times)]
    (if (empty? chain)
      freqs
      (if-let [insert (and (> num-times 0)
                           (rules (list curr (first chain))))]
        (recur curr
               (conj chain insert)
               freqs
               (dec num-times)
               (conj num-times-stack (dec num-times)))
        (recur (first chain)
               (rest chain)
               (update freqs (first chain) inc*)
               (first num-times-stack)
               (rest num-times-stack))))))

;;;

(defn all-keys [rules]
  (->> rules
       keys
       (apply concat)
       (concat (vals rules))
       set))

(defn add-freqs* [all-keys & ms]
  (reduce (fn [m k] (assoc m k (reduce #(+ %1 (get %2 k 0)) 0 ms)))
          {} all-keys))

(defn freqs-after-growth**
  "Returns the frequencies of elements in a chain after a number of reactions.

  Finally figured out how to handle the caching/pre-computation."
  [chain rules num-times]
  (let [elements (all-keys rules)
        add-freqs (partial add-freqs* elements)
        precomputes (reduce (fn [lookups [x y n]]
                              (assoc
                               lookups [x y n]
                               (if (zero? n)
                                 (if (= x y) {x 2} {x 1 y 1})
                                 (if-let [insert (rules [x y])]
                                   (add-freqs {insert -1}
                                              (lookups [x insert (dec n)])
                                              (lookups [insert y (dec n)]))
                                   (if (= x y) {x 2} {x 1 y 1})))))
                            {}
                            (for [n (range (inc num-times))
                                  [x y] (for [x elements y elements] [x y])]
                              [x y n]))
        overcount-correction (->> chain
                                  (drop 1)
                                  reverse
                                  (drop 1)
                                  (map (fn [x] {x -1}))
                                  (reduce add-freqs {}))]
    (->> (zip chain (drop 1 chain) (repeat num-times))
         (map precomputes)
         (apply add-freqs)
         (add-freqs overcount-correction))))

;;;

(defn add* [& args] (reduce + 0 (remove nil? args)))

(defn evolve
  ([rules pairs] (evolve rules 1 pairs))
  ([rules num-times pairs]
   (if (zero? num-times)
     pairs
     (recur rules
            (dec num-times)
            (reduce-kv (fn [m [a b] v]
                         (if-let [c (rules [a b])]
                           (-> m (update [a c] add* v) (update [c b] add* v))
                           (update m [a b] add* v))) {} pairs)))))

(defn unpair [last-element pairs]
  (reduce-kv (fn [m [a b] v] (update m a add* v)) {last-element 1} pairs))

(defn freqs-after-growth***
  "Returns the frequencies of elements in a chain after a number of reactions.

  Uses the 'counting pairs' method."
  [chain rules num-times]
  (->> (zip chain (drop 1 chain))
       frequencies
       (evolve rules num-times)
       (unpair (last chain))))

;;;

(defn file->big-small-diff
  ([filename] (file->big-small-diff filename freqs-after-growth 10))
  ([filename freqs-fn num-times]
   (let [rules (file->rules filename)
         chain (file->chain filename)
         freqs (freqs-fn chain rules num-times)]
     (- (-> freqs vals sort last) (-> freqs vals sort first)))))

(advent/defrunner 1 file->big-small-diff "Big-small diff (10)")
(advent/defrunner 2 file->big-small-diff "Big-small diff (40)" freqs-after-growth*** 40)
