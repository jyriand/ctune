(ns dsp.core
  (:require [incanter.charts :refer [xy-plot]]
            [seesaw.core :as ui]
            [seesaw.mig :refer [mig-panel]]
            [seesaw.bind :as bind]
            [dsp.audio :refer [open-mic audio-data]])
  (:import org.jfree.chart.ChartPanel)
  (:gen-class))

(defn wave-plot [data samples]
  (xy-plot (range 0 samples) data))

(def sample-rate 8000)
(def buffer-size (/ sample-rate 2))

(defn -main []
  (do
    (ui/native!)
    (open-mic sample-rate buffer-size)
    (let [plot (wave-plot @audio-data sample-rate)
          signal-chart (ChartPanel. plot)
          app-panel (mig-panel
                     :constraints []
                     :items [[signal-chart]])]
      (bind/bind audio-data
                 (bind/b-do* (fn [_] (.setChart signal-chart (wave-plot @audio-data sample-rate)))))
      (ui/invoke-later
       (-> (ui/frame :title "Frequencies"
                     :content app-panel
                     :on-close :exit)
           ui/pack! ui/show!)))))