

1 - Create a FindBy Method ( warehouseId et distance ( seuil )) ( DONE )
2 - Create their equivalenet with JPQL  (DONE)
3 - Use this Method Stream so I can get the minimum ( tour )
4 - Make a Get Endpoint in the controller 



@Query("SELECT t FROM Tour t WHERE t.wharehouse_id = :warehouseId  AND t.totalDistance < :distance");

List<Tour> findByWareHouseIdLessThanDistance(
    @requestParam("warehouseId),
    @requestParam("distance")
)


List<Tour> toursList = this.tourRepository.findByWareHouseIdAndDistance(warehouseId, distance);

Tour tour = toursList.stream()
            .min(Comparator.comparing(Tour::getTotaldistance));




