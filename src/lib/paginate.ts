import { RecordDocument } from '../models/record';

interface PagedResource {
  pageNumber: number;
  pagesTotal: number;
  size: number;
  hits: RecordDocument[];
}

export const toPagedResource = (
  data: RecordDocument[],
  page: number,
  limit: number,
  total: number
): PagedResource => {
  return {
    pageNumber: page,
    pagesTotal: ((total / limit) >> 0) + 1, // integer division
    size: data.length,
    hits: data.map(doc => doc.toObject())
  };
};
