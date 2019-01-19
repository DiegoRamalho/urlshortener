package ie.com.urlshortener.api.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ie.com.urlshortener.api.domain.ShortURL;
import ie.com.urlshortener.api.domain.StatisticsDto;


/**
 * Implementation of statistics queries.
 *
 * @author Diego
 * @since 19/01/2019
 */
@Repository
public class StatisticsRepository {

    protected static final String ATTR_CREATED_DATE = "createdDate";
    protected static final String ATTR_LAST_MODIFIED_DATE = "lastModifiedDate";
    protected static final String KEY_YEAR_MONTH_DAY = "yearMonthDay";
    protected static final String ALIAS_TOTAL = "total";
    protected static final String ATTR_KEY = "key";
    protected static final String ATTR_VALUE = "value";
    protected static final String ATTR_ID = "_id";

    private static final String YEAR_MONTH_DAY_PATTERN = "%Y-%m-%d";
    protected final MongoOperations mongoOperations;

    public StatisticsRepository(final MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    /**
     * Find the last Instant that some URL was created/updated.
     *
     * @return the last created/update Instant.
     */
    public ShortURL findShortURLWithLastUpdate() {
        return mongoOperations.findOne(new Query()
                                               .with(new Sort(Sort.Direction.DESC, ATTR_LAST_MODIFIED_DATE))
                                               .limit(1),
                                       ShortURL.class);
    }

    /**
     * Get the 100 days with more registrations.
     *
     * @return List of 100 days with more registrations.
     */
    public List<StatisticsDto> findTop100CreationDate() {

        final Aggregation agg = Aggregation.newAggregation(
                Aggregation.project().and(ATTR_CREATED_DATE).dateAsFormattedString(YEAR_MONTH_DAY_PATTERN).as(KEY_YEAR_MONTH_DAY),
                Aggregation.group(KEY_YEAR_MONTH_DAY).count().as(ALIAS_TOTAL),
                Aggregation.sort(Sort.Direction.DESC, ALIAS_TOTAL),
                Aggregation.project()
                        .andExpression(ATTR_ID).as(ATTR_KEY)
                        .andExpression(ALIAS_TOTAL).as(ATTR_VALUE),
                Aggregation.limit(100));

        //Convert the aggregation result into a List
        final AggregationResults<StatisticsDto> groupResults = mongoOperations.aggregate(agg, ShortURL.class, StatisticsDto.class);

        return groupResults.getMappedResults();
    }
}
