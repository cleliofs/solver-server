create table if not exists solver_execution
(
    id number(20,0) primary key not null,
--    application_id varchar2(50) not null,
--    user_id varchar2(50) not null,
--    jms_correlation_id varchar2(250) not null,
--    status varchar2(10) not null,
    model_name varchar2(50)
    params varchar2(250),
    start_date date default current_time() not null,
    end_date date
);