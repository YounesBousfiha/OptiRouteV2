package com.optiroute.optiroute.domain.event;

import com.optiroute.optiroute.domain.entity.Tour;
import lombok.Getter;


@Getter
public class TourCompletedEvent {

    private final Tour completedTour;

    public TourCompletedEvent(Tour completedTour) {
        this.completedTour = completedTour;
    }
}
