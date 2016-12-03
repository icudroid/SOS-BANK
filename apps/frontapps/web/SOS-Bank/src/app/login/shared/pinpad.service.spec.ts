/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { PinpadService } from './pinpad.service';

describe('PinpadService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PinpadService]
    });
  });

  it('should ...', inject([PinpadService], (service: PinpadService) => {
    expect(service).toBeTruthy();
  }));
});
