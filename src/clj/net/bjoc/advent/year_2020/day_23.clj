(ns net.bjoc.advent.year-2020.day-23
  (:require [clojure.string :as str]
            [taoensso.timbre :as timbre
             ;; :refer [log  trace  debug  info  warn  error  fatal  report
             ;;         logf tracef debugf infof warnf errorf fatalf reportf
             ;;         spy get-env]])
             :refer [trace debug info warn error fatal]]
            ))

(defn tee [x]
  (println x)
  x)

;;;

(defn move
  ([cups] (move cups 1))
  ([cups moves-left]
   (let [moves-left (dec moves-left)
         current (first cups)
         max-cup (reduce max cups)
         remaining (concat (take 1 cups)
                           (->> cups (drop 4)))
         removed (->> cups (take 4) (drop 1))
         in-remaining? (set remaining)
         dest (loop [dest (-> cups first dec)]
                (or (in-remaining? dest)
                    (recur (if (neg? dest) max-cup (dec dest)))))
         remaining2 (->> remaining
                         (#(concat % %))
                         (drop-while #(not= % dest))
                         (drop 1)
                         (take (-> remaining count dec)))
         result (->> (concat [dest] removed remaining2)
                     (#(concat % %))
                     (drop-while #(not= % current))
                     (drop 1)
                     (take (count cups))
                     vec)
         ]
     {:cups cups
      :removed removed
      :remaining remaining
      :dest dest
      :remaining2 remaining2
      :result result}
;;     (vprintln result)
     (if (pos? moves-left)
       (recur result moves-left)
       result))))

(defn part-1 []
  (println
   "Day 22 - Part 1 - asdf asdf:"
   ))





(defn move*
  ([cups] (move cups 1))
  ([cups moves-left]
   (let [moves-left (dec moves-left)
         current (first cups)
         remaining (-> (subvec cups 4)
                       (conj current))
         removed (subvec cups 1 4)
         ;; in-removed? (set removed)
         dest (loop [dest (dec current)]
                (if (or (= dest (nth removed 0))
                        (= dest (nth removed 1))
                        (= dest (nth removed 2))
                        (< dest 1))
                  (recur (if (pos? dest) (dec dest) (count cups)))
                  dest))
         dest-idx (.indexOf remaining dest)
         ;; _ (println dest dest-idx)
         result (-> (concat (subvec remaining 0 (inc dest-idx))
                            removed
                            (subvec remaining (inc dest-idx)))
                    vec)]
     ;; (vprintln result)
     (if (pos? moves-left)
       (recur result moves-left)
       result))))

(comment (time
          (->> "389125467"
               (#(str/split % #""))
               (map #(Integer/parseInt %))
               (#(concat % (range 10 1000001)))
               vec
               (#(move* % 200))
               (#(concat % %))
               (drop-while #(not= 1 %))
               (drop 1)
               (take 2)
               (tee)
               (reduce *)
               (tee)))

         (def ans *1)
         (take (dec (count my-cups))
               (map str)
               str/join
               println)
         )



(comment
  (def my-cups
    (let [raw-input "389125467"
          cups (->> (str/split raw-input #"")
                    (map #(Integer/parseInt %)))]
      cups))

  (def verbose true)
  (def verbose false)
  (println "===")
  (move my-cups)
  (move my-cups 10)

  (->> "389125467"
       (#(str/split % #""))
       (map #(Integer/parseInt %))
       vec
       tee
       (#(move % 10))
       (#(concat % %))
       (drop-while #(not= 1 %))
       (drop 1)
       (take (dec (count my-cups)))
       (map str)
       str/join
       println)

  (->> "398254716"
       (#(str/split % #""))
       (map #(Integer/parseInt %))
       vec
       (#(move % 100))
       (#(concat % %))
       (drop-while #(not= 1 %))
       (drop 1)
       (take (dec (count my-cups)))
       (map str)
       str/join
       println)
  )






(defn part-2 []
  (println
   "Day 22 - Part 2 - asdf asdf:"
   ))
