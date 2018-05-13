
-- Get product questionnaires
SELECT p.apir, f.dtd, f.period_end, f.activate, f.prompt, f.due, f.close, f.last_ts as submitDate ,
      f.complete, f.status, f.type  from rfi_rfi_product r 
      left join rfi f on r.rfi=f.uid
       right join rfi_product p on r.product = p.uid
     -- where apir = 'CSA0131AU'
      --f.period_end = '2017-09-30' -- and f.company = '71676f1beac675079ede80b7adcb09ea' 
      --and r.type = 'annsecsmallcap'
     -- and  apir = 'CSA0131AU'
      order by apir, f.period_end limit 100

-- Get questionnaire templates by type
    SELECT distinct dtd, type FROM public.rfi;
    