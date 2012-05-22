package com.bazaarvoice.soa.loadbalance;

import com.bazaarvoice.soa.ServiceEndpoint;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RandomAlgorithmTest {
    @Test(expected = NullPointerException.class)
    public void testNullIterable() {
        new RandomAlgorithm().choose(null);
    }

    @Test
    public void testEmptyIterable() {
        ServiceEndpoint endpoint = new RandomAlgorithm().choose(Collections.<ServiceEndpoint>emptyList());
        assertNull(endpoint);
    }

    @Test
    public void testAllElementsCanBeSelected() {
        RandomAlgorithm algorithm = new RandomAlgorithm();

        List<ServiceEndpoint> endpoints = Lists.newArrayList(
                new ServiceEndpoint("Service1", "server", 1),
                new ServiceEndpoint("Service2", "server", 2),
                new ServiceEndpoint("Service3", "server", 3),
                new ServiceEndpoint("Service4", "server", 4),
                new ServiceEndpoint("Service5", "server", 5)
        );

        // Make 100 independent choices.  This ensures that we'll see all of the elements with a probability
        // of 1-5(4/5)^1000.  If we ran this test once we would expect to see a failure every 5e88 years.
        // For our purposes that's close enough to 100%.
        Set<ServiceEndpoint> seen = Sets.newIdentityHashSet();
        for (int i = 0; i < 1000; i++) {
            seen.add(algorithm.choose(endpoints));
        }

        assertEquals(endpoints.size(), seen.size());
    }
}
