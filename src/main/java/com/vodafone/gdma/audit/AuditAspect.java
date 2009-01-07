package com.vodafone.gdma.audit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.framework.adapter.UnknownAdviceTypeException;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.User;
import org.springframework.util.CollectionUtils;

import com.vodafone.gdma.dao.AuditHeaderDao;
import com.vodafone.gdma.dao.AuditRecordDao;
import com.vodafone.gdma.domain.AuditHeader;
import com.vodafone.gdma.domain.AuditRecord;
import com.vodafone.gdma.web.command.ColumnUpdate;
import com.vodafone.gdma.web.command.UpdateRequest;

@Aspect
public class AuditAspect {

    protected static Logger LOG = Logger.getLogger(AuditAspect.class);

    private AuditHeaderDao auditHeaderDao;

    public void setAuditHeaderDao(AuditHeaderDao auditHeaderDao) {
        this.auditHeaderDao = auditHeaderDao;
    }

    public void setAuditRecordDao(AuditRecordDao auditRecordDao) {
        this.auditRecordDao = auditRecordDao;
    }

    private AuditRecordDao auditRecordDao;

    @Around("@annotation(com.vodafone.gdma.audit.Auditable)")
    public Object auditRecord(ProceedingJoinPoint pjp) throws Throwable {
        Object retVal = pjp.proceed();
        try {
            Object[] args = pjp.getArgs();
            if (args != null && args.length == 1) {
                String username = getUsername();
                Calendar today = Calendar.getInstance();
                if (args[0] instanceof UpdateRequest) {
                    char auditType = getAuditType(pjp.getSignature().toShortString());
                    auditUpdateRequest(auditType, (UpdateRequest) args[0]);
                }
            }
        } catch (Throwable e) {
            // catch all errors and log
            LOG.error(e, e);
        }
        return retVal;
    }

    private void auditUpdateRequest(char auditType, UpdateRequest updateRequest) {

        Date now = new Date();
        String username = getUsername();

        List<List<ColumnUpdate>> columnUpdates = updateRequest.getUpdates();
        if (CollectionUtils.isEmpty(columnUpdates)) {
            return;
        }

        List<AuditRecord> auditRecords = new ArrayList<AuditRecord>();

        for (List<ColumnUpdate> list : columnUpdates) {

            if (CollectionUtils.isEmpty(list)) {
                continue;
            }

            AuditHeader auditHeader = new AuditHeader();
            auditHeader.setModifiedBy(username);
            auditHeader.setModifiedOn(now);
            auditHeader.setTableID(updateRequest.getTableId());
            auditHeader.setType(auditType);
            auditHeader = auditHeaderDao.saveAuditHeader(auditHeader);

            for (ColumnUpdate columnUpdate : list) {
                // only save if values are different

                if (columnUpdate.getOldColumnValue() == columnUpdate.getNewColumnValue()) {
                    continue;
                }

                // this column wasn't modified
                if (auditType != 'M' && columnUpdate.getNewColumnValue() == null) {
                    continue;
                }

                AuditRecord auditRecord = new AuditRecord();
                auditRecord.setColumnID(columnUpdate.getColumnId());
                auditRecord.setAuditHeaderID(auditHeader.getId());
                if (auditType != 'D') {
                    auditRecord.setNewValue(columnUpdate.getNewColumnValue());
                }
                if (auditType != 'A') {
                    auditRecord.setOldValue(columnUpdate.getOldColumnValue());
                }
                auditRecords.add(auditRecord);
            }

        }
        if (!CollectionUtils.isEmpty(auditRecords)) {
            auditRecordDao.saveAuditRecords(auditRecords);
        }

    }

    private char getAuditType(String signature) {
        signature = signature.toUpperCase();
        if (signature.indexOf("UPDATE") > -1) {
            return 'M';
        } else if (signature.indexOf("DELETE") > -1) {
            return 'D';
        } else if (signature.indexOf("ADD") > -1) {
            return 'A';
        } else {
            throw new UnknownAdviceTypeException("Audit type not identified in signature " + signature);
        }
    }

    private String getUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            LOG.debug("User ==> NULL");
            return "NULL";
        }
        User user = (User) authentication.getPrincipal();
        LOG.debug("User ==> " + user.getUsername() + "\n");
        return user.getUsername();
    }

}
