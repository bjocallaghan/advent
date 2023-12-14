(ns net.bjoc.advent.util.numeric)

(defn int-str?
  "Return truthy if the input is a parsable integer string."
  [x]
  (and (string? x)
       (re-find #"^-?\d+$" x)))

(def parse-int "Convert a string to an integer." #(Integer/parseInt %))

(defn int-seq
  "Return an infinite sequence of integers."
  [starting-n]
  (lazy-seq (cons starting-n (int-seq (inc starting-n)))))

(defn digit-seq
  "Return a sequence of the single digits of a given number."
  [n]
  (loop [n n
         digits []]
    (if (zero? n)
      (reverse digits)
      (recur (quot n 10) (conj digits (mod n 10))))))

(defn exp
  "Raise a number to a power."
  [number exponent]
  (reduce * (repeat exponent number)))

(defn TRIAL_DIVISION*not-prime?
  "Return truthy for a non-prime number.

  The return value is generally (but not guaranteed) to be a divisor.

  Uses the trial division method."
  [n]
  (cond
    (= n 2) nil
    (= n 3) nil
    (< n 2) -1
    (even? n) 2
    :trial-division (let [limit (-> n Math/sqrt int inc)]
                      (loop [divisor 3]
                        (if (zero? (rem n divisor))
                          divisor
                          (when (< divisor limit)
                            (recur (+ divisor 2))))))))

(def TRIAL_DIVISION*prime? "Return truthy for a prime number."
  (complement TRIAL_DIVISION*not-prime?))

;;; defaults

(def prime? "Returns truthy for a prime number."
  TRIAL_DIVISION*prime?)

(def not-prime? "Return truthy for a non-prime number."
  TRIAL_DIVISION*not-prime?)

(defn prime-seq
  "Return an infinite sequence of prime numbers."
  []
  (->> (int-seq 2)
       (filter prime?)))

(defn is-divisible-by
  "Return truthy if `n` is divisible by `divisor`."
  [n divisor]
  (zero? (mod n divisor)))

(defn prime-factorization
  "Return the prime factorization of `n` as a map of prime factors and their exponents."
  [n]
  (let [get-cutoff #(-> % Math/sqrt long inc)
        candidate-factors (take-while #(< % n) (prime-seq))
        ans (->> candidate-factors
                 (map (fn [factor]
                        [factor
                         (loop [times 0
                                n n]
                           (if (not (is-divisible-by n factor))
                             times
                             (recur (inc times)
                                    (/ n factor))))]))
                 (remove (fn [[_ times]] (zero? times)))
                 (into {}))]
    (if (empty? ans)
      {n 1}
      ans)))

(def ^:private max* #(if (nil? %1) %2 (max %1 %2)))

(defn lcm
  "Return the least common multiple of a set of numbers."
  [num & nums]
  (let [nums (conj nums num)]
    (->> nums
         (mapcat prime-factorization)
         (reduce (fn [m [d times]]
                   (update m d max* times))
                 {})
         (mapcat (fn [[d times]] (repeat times d)))
         (reduce *))))
