import { SOSBankPage } from './app.po';

describe('sos-bank App', function() {
  let page: SOSBankPage;

  beforeEach(() => {
    page = new SOSBankPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
