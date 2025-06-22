package com.jammit_be.gathering.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jammit_be.common.enums.BandSession;
import com.jammit_be.gathering.entity.GatheringSession;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class GatheringSessionRequest {

    @JsonProperty("bandSession")
    private BandSession bandSession;
    
    @JsonProperty("recruitCount")
    private int recruitCount;

    public GatheringSession toEntity() {
        if (bandSession == null) {
            log.error("bandSession is null!");
        }
        return GatheringSession.create(bandSession, recruitCount);
    }
}
