(ns net.bjoc.advent.year-2020.day-24
  (:require [clojure.string :as str]))

(defn parse-line [line]
  (loop [chars (seq line)
         tokens []]
    (if (empty? chars)
      tokens
      (let [len (if (#{\e \w} (first chars)) 1 2)]
        (recur (drop len chars)
               (conj tokens (keyword (str/join (take len chars)))))))))

(defn normalize-line [parsed-line]
  (let [m (merge {:e 0 :w 0 :ne 0 :nw 0 :se 0 :sw 0}
                 (frequencies parsed-line))
        m (-> (update m :e #(- % (:w m)))
              (dissoc :w))
        m (-> (update m :se #(- % (:nw m)))
              (dissoc :nw))
        m (-> (update m :ne #(- % (:sw m)))
              (dissoc :sw))
        m (-> (update m :ne #(- % (:se m)))
              (update :e #(+ % (:se m)))
              (dissoc :se))
        ]
    m))

(defn file->num-flips [filename]
  (->> (str/split (slurp filename) #"\n")
       (map parse-line)
       (map normalize-line)
       frequencies
       (remove #(even? (second %)))
       count))

(defn part-1 []
  (println
   "Day 24 - Part 1 - Number of tiles with black side up:"
   (file->num-flips "data/year_2020/day_24.input")))

(defn adjacents [[e-coord ne-coord]]
  [[(inc e-coord) ne-coord]
   [(dec e-coord) ne-coord]
   [e-coord (inc ne-coord)]
   [e-coord (dec ne-coord)]
   [(dec e-coord) (inc ne-coord)]
   [(inc e-coord) (dec ne-coord)]])

(defn file->flips [filename]
  (->> (str/split (slurp filename) #"\n")
       (map parse-line)
       (map normalize-line)
       frequencies
       (remove #(even? (second %)))
       keys
       (map #(vector (:e %) (:ne %)))))

(defn evolve
  ([flips] (evolve flips 1))
  ([flips num-rounds]
   (if (pos? num-rounds)
     (let [candidates (into #{} (mapcat adjacents flips))
           flipped? (into #{} flips)]
       (recur (filter (fn [candidate]
                        (let [num-neighbors (->> (adjacents candidate)
                                                 (filter flipped?)
                                                 count)]
                          (if (flipped? candidate)
                            (or (= num-neighbors 1)
                                (= num-neighbors 2))
                            (= num-neighbors 2))))
                      candidates)
              (dec num-rounds)))
     flips)))

(defn file->num-evolved-flips [filename num-rounds]
  (->> (file->flips filename)
       (#(evolve % num-rounds))
       count))

(defn part-2 []
  (println
   "Day 24 - Part 2 - Number of tiles with black side up after 100 evolutions:"
   (file->num-evolved-flips "data/year_2020/day_24.input" 100)))
