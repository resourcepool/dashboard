package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.service.impl.RevisionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "dashboard")
public class DashboardController {

    @Autowired
    private RevisionServiceImpl revisionService;

    /**
     * Get the number of the latest revision
     * @return long representing latest revision
     */
    @RequestMapping(value = "revision", method = RequestMethod.GET)
    public long getDiffs() {
        return revisionService.getLatest();
    }

    @RequestMapping(value = "{revision}", method = RequestMethod.GET)
    public List<Revision> getDiffs(@PathVariable("revision") long revision) {
        return revisionService.getDiffs(revision);
    }


}