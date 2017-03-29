package s2jh.biz.shop.support.data;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import lab.s2jh.core.data.DatabaseDataInitializeProcessor;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.util.MockEntityUtils;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.service.RoleService;
import lab.s2jh.module.auth.service.UserService;
import lab.s2jh.module.schedule.service.JobBeanCfgService;
import lab.s2jh.module.sys.entity.ConfigProperty;
import lab.s2jh.module.sys.service.ConfigPropertyService;
import lab.s2jh.module.sys.service.DataDictService;
import lab.s2jh.support.service.DynamicConfigService;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import s2jh.biz.shop.cons.BizConstant;
import s2jh.biz.shop.entity.Order;
import s2jh.biz.shop.entity.SiteUser;
import s2jh.biz.shop.service.OrderService;
import s2jh.biz.shop.service.SiteUserService;

import com.google.common.collect.Lists;

/**
 * 业务数据初始化处理器
 */
@Component
public class BizDatabaseDataInitializeProcessor extends DatabaseDataInitializeProcessor {

    private final static Logger logger = LoggerFactory.getLogger(BizDatabaseDataInitializeProcessor.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ConfigPropertyService configPropertyService;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private JobBeanCfgService jobBeanCfgService;

    @Autowired
    private SiteUserService siteUserService;

    @Autowired
    private OrderService orderService;

    @Override
    public void initializeInternal() {

        if (configPropertyService.findByPropKey(BizConstant.CFG_HTML_FAQ) == null) {
            ConfigProperty entity = new ConfigProperty();
            entity.setPropKey(BizConstant.CFG_HTML_FAQ);
            entity.setPropName("常见问题文案");
            entity.setHtmlValue(getStringFromTextFile("faq.txt"));
            configPropertyService.save(entity);
        }

        //演示模式创建一些模拟数据
        if (DynamicConfigService.isDemoMode()) {

            logger.debug("Prepare data for DEMO mode...");

            //获取一些随机图片集合数据
            List<String> randomImages = Lists.newArrayList();
            URL url = this.getClass().getResource("images");
            String fileName = url.getFile();
            Collection<File> files = FileUtils.listFiles(new File(fileName), null, false);
            for (File file : files) {
                randomImages.add("/files/mock/" + file.getName());
            }

            //如果为空表则初始化模拟数据，
            if (isEmptyTable(SiteUser.class)) {
                //随机注册用户数量
                int cnt = MockEntityUtils.randomInt(10, 20);
                for (int i = 0; i < cnt; i++) {
                    //随机用户注册日期: 当前系统日期之前若干天
                    DateUtils.setCurrentDate(MockEntityUtils.randomDate(90, -7));

                    //构造随机属性值填充用户对象。一般随机属性生成后，需要对一些特定业务属性特殊设置。
                    User user = MockEntityUtils.buildMockObject(User.class);
                    //基于当前循环流水号作为模拟数据账号
                    String seq = String.format("%03d", i);
                    user.setAuthUid("test" + seq);
                    user.setTrueName("测试账号" + seq);
                    //对email属性设置有效格式的值，否则无法通过实体上定义的@Email注解验证
                    user.setEmail(user.getAuthUid() + "@s2jh4net.com");
                    //调用业务接口进行模拟数据保存
                    userService.save(user, "123456");

                    SiteUser siteUser = MockEntityUtils.buildMockObject(SiteUser.class);
                    siteUser.setUser(user);
                    //随机注册头像头像
                    siteUser.setHeadPhoto(MockEntityUtils.randomCandidates(randomImages));
                    siteUserService.save(siteUser);

                    //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
                    commitAndResumeTransaction();

                    //随机模拟用户下单
                    int orderCount = MockEntityUtils.randomInt(0, 5);
                    for (int j = 0; j < orderCount; j++) {
                        //新事务中重新查询加载对象
                        siteUser = siteUserService.findOne(siteUser.getId());

                        //构造模拟订单对象
                        Order order = new Order();
                        //模拟订单号
                        order.setOrderNo("O" + siteUser.getId() + j);
                        order.setSiteUser(siteUser);

                        //模拟用户在注册后随机时间下单
                        DateUtils.setCurrentDate(new DateTime(siteUser.getUser().getUserExt().getSignupTime()).plusHours(
                                MockEntityUtils.randomInt(1, 240)).toDate());
                        orderService.submitOrder(order);
                        //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
                        commitAndResumeTransaction();

                        //随机部分订单支付
                        if (MockEntityUtils.randomBoolean()) {
                            //新事务中重新查询加载对象
                            order = orderService.findOne(order.getId());
                            //设置付款时间为当前订单的下单时间之后的随机1到8小时的时间点
                            Date randomTime = new DateTime(order.getSubmitTime()).plusHours(MockEntityUtils.randomInt(1, 8)).toDate();
                            DateUtils.setCurrentDate(randomTime);
                            orderService.payOrder(order);
                            //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
                            commitAndResumeTransaction();
                        }
                    }
                }
            }
            //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
            commitAndResumeTransaction();
        }
    }
}
