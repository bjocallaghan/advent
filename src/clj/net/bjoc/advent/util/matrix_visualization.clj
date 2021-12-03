(ns net.bjoc.advent.util.matrix-visualization
  (:import [java.awt Color Graphics Dimension]
           [java.awt.image BufferedImage]
           [javax.swing JPanel JFrame]
           [javax.imageio ImageIO])
  (:require [clojure.string :as str]
            [net.bjoc.advent.util.matrix :as mtx]))

(defn- palletted-paint-char-value
  [pallette g x-min y-min width height value]
  (when-let [c (pallette value)]
    (doto g
      (.setColor c)
      (.fillRect x-min y-min width height))))

(defn- rand-color []
  (. Color getHSBColor (rand) (+ 0.25 (rand 0.5)) (+ 0.25 (rand 0.5))))

(defn- generate-color-seq []
  (let [infinite-rand-colors
        (fn rand-color-seq []
          (lazy-seq (cons (rand-color) (rand-color-seq))))]
    (concat (list 
             (. Color RED)
             (. Color BLUE)
             (. Color YELLOW)
             (. Color PINK)
             (. Color CYAN)
             (. Color GREEN)
             (. Color ORANGE)
             (. Color MAGENTA))
            (infinite-rand-colors))))

(defn- generate-pallette
  "Generate a semi-random map of colors for the values present in a matrix."
  [matrix]
  (into {} (map vector (-> matrix vals set) (generate-color-seq))))

(defn- guess-paint-value-fn
  "Attempts to guess an appropriate function for filling the element cells based
  on the provided options or the values found inside matrix."
  [matrix
   & {:keys [pallette]
      :as options}]
  (cond
    pallette (partial palletted-paint-char-value pallette)
    :else
    (case (mtx/guess-type matrix)
      :characters (partial palletted-paint-char-value (generate-pallette matrix))
      )))

(defn image
  "Return a bitmap image of a matrix."
  [matrix
   & {:keys [paint-value-fn img-size background-color]
      :or {img-size 500
           background-color (. Color white)}
      :as options}]
  (let [paint-value-fn (or paint-value-fn
                           (apply (partial guess-paint-value-fn matrix)
                                  (mapcat identity options)))
        scale (/ img-size (mtx/size matrix))
        img (new BufferedImage img-size img-size
                 (. BufferedImage TYPE_INT_ARGB))
        g (. img (getGraphics))]
    (doto g
      (.setColor background-color)
      (.fillRect 0 0 img-size img-size))
    (dorun
     (for [[[x y] v] matrix]
       (paint-value-fn g (* x scale) (* y scale) scale scale v)))
    img))

(defn write-image-file
  "Save an image of a matrix to a file."
  [matrix dest-filename & options]
  (when-not (re-find #"\.png$" dest-filename)
    (throw (ex-info "Destination filename must end in '.png'"
                    {:filename dest-filename})))
  (let [img (apply (partial image matrix)
                   (mapcat identity options))]
    (. ImageIO write img "png" (java.io.File. dest-filename))
    img))

(defn- as-xy [dimension]
  [(int (.getWidth dimension)) (int (.getHeight dimension))])

(defn show
  "Display an image of a matrix in a new window.

  Although the simple process is: generate the image of the matrix and slap it
  on a paneled frame, for performance reasons we only want to figure out the
  'paint-value-fn' stuff once. Therefore the implementation has some [hard to
  understand?] pre-computation stuff.

  Additionally, the implementation is further complicated by code to keep the
  image centered in the frame."
  [matrix
   & {:keys [window-size paint-value-fn]
      :or {window-size 800}
      :as options}]
  (let [paint-value-fn (or paint-value-fn
                           (apply (partial guess-paint-value-fn matrix)
                                  (mapcat identity options)))
        img-fn #(apply (partial image matrix)
                       (mapcat identity (-> options
                                            (assoc :paint-value-fn paint-value-fn)
                                            (assoc :img-size %))))
        paint-panel-fn (fn [panel g]
                         (let [[x y] (as-xy (.getSize panel))
                               lim (min x y)
                               x-offset (-> x (- lim) (quot 2))
                               y-offset (-> y (- lim) (quot 2))
                               img (img-fn lim)]
                           (. g (drawImage img x-offset y-offset nil))
                           (.dispose (. img (getGraphics)))))
        panel (doto (proxy [JPanel] []
                      (paint [g]
                        (paint-panel-fn this g)))
                (.setPreferredSize (new Dimension window-size window-size)))
        frame (new JFrame)]
    (doto frame (.add panel) .pack .show)
    [frame panel]))
