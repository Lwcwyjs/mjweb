package com.service.sysmanage;

import com.commons.result.OrgTreeVo;
import com.commons.result.TreeVo;
import com.commons.utils.StringUtils;
import com.model.sysmanage.SysOrganization;
import com.service.base.PublicService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysOrganizationServiceImpl implements SysOrganizationService {
    @Autowired
    private PublicService publicService;

    @Override
    public List<TreeVo> findTree(String code, String organtype) {
        List<TreeVo> trees = new ArrayList<TreeVo>();
        organtype = organtype == null ? "" : organtype;
        String[] organType = organtype.split(",");

        List<SysOrganization> organs = publicService.selectObjs("ai_sys_organization", "", "", "porgan asc,seq asc", new SysOrganization());

        for (SysOrganization organ : organs) {
            String otype = organ.getOrgantype();
            otype = otype == null ? "" : otype;

            if (organ.getOrgan().equals(code) && (StringUtils.isBlank(organtype) || ArrayUtils.contains(organType, otype))) {
                TreeVo tree = new TreeVo();

                tree.setId(organ.getOrgan());
                tree.setText(organ.getName());

                tree.setChildren(findChildren(organs, organ.getOrgan(), organtype));
                trees.add(tree);
            }
        }

        return trees;
    }

    @Override
    public List<SysOrganization> findOrganAll(String porgan, String organ) {
        List<SysOrganization> organList = new ArrayList();
        String strWhere = "";
        if (!porgan.equals(organ)) {
            if (porgan != null) {
                strWhere = String.format("organ='%s'", porgan, porgan);
            } else {
                strWhere = "1>1";
            }
            SysOrganization sysOrganizationporgan = publicService.selectObj("ai_sys_organization", "", "", "porgan,seq", strWhere,
                    new SysOrganization());
            if (sysOrganizationporgan != null) {
                organList.add(sysOrganizationporgan);
            }
        }
        if (organ != null) {
            strWhere = String.format("organ='%s' or porgan='%s'", organ, organ);
        } else {
            strWhere = "1>1";
        }
        List<SysOrganization> sysOrganizations = publicService.selectObjs("ai_sys_organization", "", "", "porgan,seq", strWhere,
                new SysOrganization());
        for (SysOrganization sysOrganization : sysOrganizations) {
            organList.add(sysOrganization);
            if (!sysOrganization.getOrgan().equals(organ)) {
                strWhere = String.format("porgan='%s'", sysOrganization.getOrgan());
                List<SysOrganization> rs = publicService.selectObjs("ai_sys_organization", "", "", "porgan,seq", strWhere,
                        new SysOrganization());
                if (rs.size() > 0) {
                    for (int i = 0; i < rs.size(); i++) {
                        organList.add(rs.get(i));
                        strWhere = String.format("porgan='%s'", rs.get(i).getOrgan());
                        List<SysOrganization> rssid = publicService.selectObjs("ai_sys_organization", "", "", "porgan,seq", strWhere,
                                new SysOrganization());
                        if (rssid.size() > 0) {
                            for (int m = 0; m < rssid.size(); m++) {
                                organList.add(rssid.get(m));
                                strWhere = String.format("porgan='%s'", rssid.get(m).getOrgan());
                                List<SysOrganization> sid = publicService.selectObjs("ai_sys_organization", "", "", "porgan,seq", strWhere,
                                        new SysOrganization());
                                if (sid.size() > 0) {
                                    organList.addAll(sid);
                                }
                            }
                        }
                    }
                }
            }
        }
        return organList;
    }

    @Override
    public List<OrgTreeVo> findOrgTreeForOrgan(String code, String organtype) {
        List<OrgTreeVo> trees = new ArrayList<OrgTreeVo>();
        organtype = organtype == null ? "" : organtype;
        String[] organType = organtype.split(",");

        List<SysOrganization> organs = publicService.selectObjs("ai_sys_organization", "", "", "porgan asc,seq asc", new SysOrganization());

        for (SysOrganization organ : organs) {
            String otype = organ.getOrgantype();
            otype = otype == null ? "" : otype;
            if (organ.getOrgan().equals(code) && (StringUtils.isBlank(organtype) || ArrayUtils.contains(organType, otype))) {
                OrgTreeVo tree = new OrgTreeVo();

                tree.setId(organ.getOrgan());
                tree.setTitle(organ.getJc());
                tree.setSpread(true);
                tree.setPid(organ.getPorgan());

                tree.setChildren(findOrgChildrenForOrgan(organs, organ.getOrgan(), organtype));
                trees.add(tree);
            }
        }

        return trees;
    }

    private List<OrgTreeVo> findOrgChildrenForOrgan(List<SysOrganization> organs, String porgan, String organtype) {
        List<OrgTreeVo> trees = new ArrayList<OrgTreeVo>();
        organtype = organtype == null ? "" : organtype;
        String[] organType = organtype.split(",");

        for (SysOrganization organ : organs) {
            String oporgan = organ.getPorgan();
            String otype = organ.getOrgantype();
            oporgan = oporgan == null ? "" : oporgan;
            otype = otype == null ? "" : otype;
            if (oporgan.equals(porgan) && (StringUtils.isBlank(organtype) || ArrayUtils.contains(organType, otype))) {
                OrgTreeVo tree = new OrgTreeVo();

                tree.setId(organ.getOrgan());
                tree.setTitle(organ.getJc());
                tree.setSpread(true);
                tree.setPid(organ.getPorgan());

                tree.setChildren(findOrgChildren(organs, organ.getOrgan(), organtype));
                trees.add(tree);
            }
        }

        return trees;
    }

    @Override
    public List<OrgTreeVo> findOrgTree(String code, String organtype) {
        List<OrgTreeVo> trees = new ArrayList<OrgTreeVo>();
        organtype = organtype == null ? "" : organtype;
        String[] organType = organtype.split(",");

        List<SysOrganization> organs = publicService.selectObjs("ai_sys_organization", "", "", "porgan asc,seq asc", new SysOrganization());

        for (SysOrganization organ : organs) {
            String otype = organ.getOrgantype();
            otype = otype == null ? "" : otype;
            String status = organ.getStatus();
            //停止状态不加入列表
            if (status == null) {
                continue;
            }
            if (status.equals("0")) {
                continue;
            }
            String organno = organ.getOrgan();

//            if (!sqm.equals(enCode)) {
//                continue;
//            }
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Map<String, String> map = auth.checkCode(organno, "00", sdf.format(new Date()), sqm);
//            if (map.get("code").equals("1")) {
            if (StringUtils.isBlank(organtype) || ArrayUtils.contains(organType, otype)) {
                OrgTreeVo tree = new OrgTreeVo();

                tree.setId(organ.getOrgan());
                tree.setTitle(organ.getJc());
                tree.setSpread(true);
                tree.setPid(organ.getPorgan());

                tree.setChildren(findOrgChildren(organs, organ.getOrgan(), organtype));
                trees.add(tree);
            }
//            }
        }

        return trees;
    }

    private List<OrgTreeVo> findOrgChildren(List<SysOrganization> organs, String porgan, String organtype) {
        List<OrgTreeVo> trees = new ArrayList<OrgTreeVo>();
        organtype = organtype == null ? "" : organtype;
        String[] organType = organtype.split(",");

        for (SysOrganization organ : organs) {
            String oporgan = organ.getPorgan();
            String otype = organ.getOrgantype();
            oporgan = oporgan == null ? "" : oporgan;
            otype = otype == null ? "" : otype;
            String status = organ.getStatus();
            //停止状态不加入列表
            if (status == null) {
                continue;
            }
            if (status.equals("0")) {
                continue;
            }
            String organno = organ.getOrgan();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (oporgan.equals(porgan) && (StringUtils.isBlank(organtype) || ArrayUtils.contains(organType, otype))) {
                OrgTreeVo tree = new OrgTreeVo();

                tree.setId(organ.getOrgan());
                tree.setTitle(organ.getJc());
                tree.setSpread(true);
                tree.setPid(organ.getPorgan());

                tree.setChildren(findOrgChildren(organs, organ.getOrgan(), organtype));
                trees.add(tree);
            }
        }

        return trees;
    }

    private List<TreeVo> findChildren(List<SysOrganization> organs, String porgan, String organtype) {
        List<TreeVo> trees = new ArrayList<TreeVo>();
        organtype = organtype == null ? "" : organtype;
        String[] organType = organtype.split(",");

        for (SysOrganization organ : organs) {
            String oporgan = organ.getPorgan();
            String otype = organ.getOrgantype();
            oporgan = oporgan == null ? "" : oporgan;
            otype = otype == null ? "" : otype;
            if (oporgan.equals(porgan) && (StringUtils.isBlank(organtype) || ArrayUtils.contains(organType, otype))) {
                TreeVo tree = new TreeVo();

                tree.setId(organ.getOrgan());
                tree.setText(organ.getJc());

                tree.setChildren(findChildren(organs, organ.getOrgan(), organtype));
                trees.add(tree);
            }
        }

        return trees;
    }

    @Override
    public void deleteOrganizationByCode(String code) {
        List<SysOrganization> organs = findChildren(code, new ArrayList<SysOrganization>());

        // 删除组织机构
        SysOrganization organization = new SysOrganization();
        organization.setOrgan(code);
        publicService.delete("ai_sys_organization", "", "organ", organization);

        // 删除下属组织机构
        for (SysOrganization organ : organs) {
            publicService.delete("ai_sys_organization", "", "organ", organ);
        }
    }

    @Override
    public List<SysOrganization> findOrganizationsByCode(String code) {

        List<SysOrganization> organs = publicService.selectObjs("ai_sys_organization", "", "", new SysOrganization());

        List<SysOrganization> organizations = new ArrayList<SysOrganization>();

        for (SysOrganization organization : organs) {
            if (organization.getOrgan() == code) {
                organizations.add(organization);

                List<SysOrganization> os = findChildren(organization.getOrgan(), organs);
                for (SysOrganization o : os) {
                    organizations.add(o);
                }
            }
        }

        return organizations;
    }
    @Override
    public String getQymcByQybh(String qybh) {
        String qymc=qybh;
        SysOrganization sysOrganization=new SysOrganization();
        sysOrganization.setOrgan(qybh);
        sysOrganization= publicService.selectObj("ai_sys_organization", "", "organ",sysOrganization);

        if (sysOrganization!=null){
            qymc=sysOrganization.getName();
        }
        return qymc;
    }
    @Override
    public String getQyjcByQybh(String qybh) {
        String qymc=qybh;
        SysOrganization sysOrganization=new SysOrganization();
        sysOrganization.setOrgan(qybh);
        sysOrganization= publicService.selectObj("ai_sys_organization", "", "organ",sysOrganization);

        if (sysOrganization!=null){
            qymc=sysOrganization.getJc();
        }
        return qymc;
    }

    @Override
    public SysOrganization findOrganizationByCode(String code) {

        SysOrganization organization = new SysOrganization();

        organization.setOrgan(code);

        return publicService.selectObj("ai_sys_organization", "", "organ", organization);
    }

    @Override
    public List<SysOrganization> findChildren(String code, List<SysOrganization> organs) {
        if (organs == null || organs.size() == 0) {
            organs = publicService.selectObjs("ai_sys_organization", "", "", new SysOrganization());
        }
        List<SysOrganization> organChildren = new ArrayList<SysOrganization>();

        for (SysOrganization organization : organs) {
            String porgan = organization.getPorgan();
            porgan = porgan == null ? "" : porgan;
            if (porgan.equals(code)) {
                organChildren.add(organization);

                List<SysOrganization> os = findChildren(organization.getOrgan(), organs);
                for (SysOrganization o : os) {
                    organChildren.add(o);
                }
            }
        }

        return organChildren;
    }

    @Override
    public String findChildrenCodes(String code) {
        String strCodes = "";
        List<SysOrganization> organChildren = findChildren(code, new ArrayList<SysOrganization>());

        for (SysOrganization organization : organChildren) {
            String status = organization.getStatus();
            //停止状态不加入列表
            if (status == null) {
                continue;
            }
            if (status.equals("0")) {
                continue;
            }
            strCodes += strCodes.equals("") ? "" : ",";
            strCodes += organization.getOrgan();
        }

        return strCodes;
    }

    @Override
    public List<SysOrganization> findAllOrgan() {
        String strCodes = "";
        List<SysOrganization> organizations = publicService.selectObjs("select * from ai_sys_organization", SysOrganization.class);
        if (organizations.size() > 0) {
            for (int i = 0; i < organizations.size(); i++) {
                String organ = organizations.get(i).getOrgan();
            }
        }
        return organizations;
    }

}
