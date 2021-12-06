(ns net.bjoc.advent.year-2015.day-1
  (:use [net.bjoc.advent.util.misc :only [find-first]]))

(defn resolve-directions [s]
  (let [h (frequencies s)]
    (- (get h \( 0) (get h \) 0))))

(defn file->floor [filename]
  (resolve-directions (slurp filename)))

(defn part-1 []
  (println
   "Day 1 - Part 1 - Floor:"
   (file->floor "data/year_2015/day_1.input")))

(defn basement-entry [s]
  (let [len (count s)]
    (->> (range (inc len))
         (map #(vector % (resolve-directions (subs s 0 %))))
         (find-first (fn [[_ floor]] (= floor -1)))
         first)))

(defn file->basement-entry [filename]
  (basement-entry (slurp filename)))

(defn part-2 []
  (println
   "Day 1 - Part 2 - Basement entry index:"
   (file->basement-entry "data/year_2015/day_1.input")))
