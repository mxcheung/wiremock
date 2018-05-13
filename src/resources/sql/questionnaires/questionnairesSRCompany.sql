
-- Get company questionnaires
    SELECT c.fund_id,  r.dtd, r.period_end, r.activate, r.prompt, r.due, r.close, r.last_ts as submitDate , r.complete, r.status,  r.type
    FROM public.rfi r , public.rfi_company c
    where r.company = c.uid
    and c.fund_id <> 5555
    and fund_id is not null
 --   order by c.uid limit 100;


-- Get product questionnaires
      SELECT c.product_id,  r.dtd, r.period_end, r.activate, r.prompt, r.due, r.close, r.last_ts as submitDate , r.complete, r.status,  r.type
    FROM public.rfi r , public.rfi_product c
    where r.company = c.company
   order by c.uid limit 100


-- Get questionnaire templates by type
    SELECT distinct dtd, type FROM public.rfi;
    